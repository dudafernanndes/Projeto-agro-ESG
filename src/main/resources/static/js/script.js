// API Configuration
// Use o mesmo host/origem que serve o frontend por padrão (evita chamadas a domínios externos)
const API_BASE_URL = window.location.origin;
let authToken = localStorage.getItem('authToken');
let currentUser = JSON.parse(localStorage.getItem('currentUser')) || null;

function apiUrl(path) {
    const base = API_BASE_URL.replace(/\/+$/, '');
    const p = String(path || '').startsWith('/') ? String(path || '') : `/${path}`;
    return `${base}${p}`;
}

// Initialize app
document.addEventListener('DOMContentLoaded', () => {
    updateUIBasedOnAuth();
});

// ----------------------------------------------------
// Navigation Functions
// ----------------------------------------------------
function navigate(section) {
    // Esconder todas as seções
    document.querySelectorAll('.section').forEach(s => s.classList.add('hidden'));

    // Redirecionamento de segurança
    if (['dashboard', 'module'].includes(section) && !authToken) {
        showError('Você precisa fazer login primeiro');
        section = 'login';
    }

    // Mostrar a seção correta
    const targetSection = document.getElementById(`sec-${section}`);
    if (targetSection) {
        targetSection.classList.remove('hidden');
    }

    // Atualizar classe ativa na navbar
    document.querySelectorAll('.nav-menu a').forEach(a => a.classList.remove('active'));
    const navLink = document.getElementById(`nav-${section}`);
    if (navLink) navLink.classList.add('active');

    // Inicializações específicas
    if (section === 'dashboard') {
        document.getElementById('userNameDisplay').textContent = currentUser?.name || 'Visitante';
    }
    if (section === 'lots') {
        loadLots();
    }
}

function openModule(moduleName) {
    navigate('module');
    
    const titles = {
        lots: 'Gerenciar Lotes',
        events: 'Eventos (visão geral)'
    };

    document.getElementById('moduleTitle').textContent = titles[moduleName] || 'Módulo';
    document.getElementById('moduleContent').innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> Carregando dados...</div>';

    if (moduleName === 'events') {
        loadLotsForEventsModule();
        return;
    }

    // Chama a função de carregamento correspondente
    loadData(moduleName);
}

function updateUIBasedOnAuth() {
    const navLogin = document.getElementById('nav-login');
    const navRegister = document.getElementById('nav-register');
    const navDashboard = document.getElementById('nav-dashboard');
    const navLogout = document.getElementById('nav-logout');

    if (authToken && currentUser) {
        if(navLogin) navLogin.classList.add('hidden');
        if(navRegister) navRegister.classList.add('hidden');
        if(navDashboard) navDashboard.classList.remove('hidden');
        if(navLogout) navLogout.classList.remove('hidden');
    } else {
        if(navLogin) navLogin.classList.remove('hidden');
        if(navRegister) navRegister.classList.remove('hidden');
        if(navDashboard) navDashboard.classList.add('hidden');
        if(navLogout) navLogout.classList.add('hidden');
    }
}

// ----------------------------------------------------
// Authentication Functions
// ----------------------------------------------------
async function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('loginEmail').value;
    const password = document.getElementById('loginPassword').value;

    try {
        const response = await fetch(apiUrl('/api/auth/login'), {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ email, password })
        });

        if (!response.ok) throw new Error('Email ou senha inválidos');

        const data = await response.json();
        
        authToken = data.token;
        currentUser = {
            userId: data.id,
            name: data.name,
            email: data.email
        };

        localStorage.setItem('authToken', authToken);
        localStorage.setItem('currentUser', JSON.stringify(currentUser));

        document.getElementById('loginForm').reset();
        updateUIBasedOnAuth();
        navigate('dashboard');
        showSuccess('Login realizado com sucesso!');
    } catch (error) {
        showError('Erro ao fazer login: ' + error.message);
    }
}

async function handleRegister(e) {
    e.preventDefault();
    const name = document.getElementById('registerName').value;
    const email = document.getElementById('registerEmail').value;
    const cpf = document.getElementById('registerCpf').value;
    const password = document.getElementById('registerPassword').value;

    try {
        const response = await fetch(apiUrl('/api/auth/register'), {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            // Backend espera: nomeCompleto, email, cpf, senha
            body: JSON.stringify({ nomeCompleto: name, email, cpf, senha: password })
        });

        if (!response.ok) throw new Error('Erro ao registrar usuário. Verifique os dados fornecidos.');

        const data = await response.json();
        authToken = data.token;
        currentUser = {
            userId: data.id,
            name: data.name,
            email: data.email
        };

        localStorage.setItem('authToken', authToken);
        localStorage.setItem('currentUser', JSON.stringify(currentUser));

        document.getElementById('registerForm').reset();
        updateUIBasedOnAuth();
        navigate('dashboard');
        showSuccess('Registrado com sucesso! Bem-vindo!');
    } catch (error) {
        showError('Erro ao registrar: ' + error.message);
    }
}

function logout() {
    authToken = null;
    currentUser = null;
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUser');
    updateUIBasedOnAuth();
    navigate('home');
    showSuccess('Logout realizado com sucesso!');
}

// ----------------------------------------------------
// Data Loading Functions
// ----------------------------------------------------
async function loadData(endpoint) {
    const container = document.getElementById('moduleContent');

    const endpointMap = {
        properties: 'properties',
        lots: 'lotes'
    };

    const resolvedEndpoint = endpointMap[endpoint] || endpoint;
    
    try {
        const response = await fetch(apiUrl(`/${resolvedEndpoint}`), {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (!response.ok) {
            if(response.status === 404) {
                 throw new Error('Recurso não encontrado (Backend pode não ter a rota ' + resolvedEndpoint + ' implementada).');
            }
            throw new Error(`Erro ao carregar (Status ${response.status})`);
        }

        const data = await response.json();
        
        let contentArray = data;
        // Tratamento para Spring HATEOAS / Paginação
        if (data._embedded) {
            // Pegar o primeiro valor dentro do _embedded dinamicamente
            const key = Object.keys(data._embedded)[0];
            contentArray = data._embedded[key];
        } else if (data.content) {
            contentArray = data.content;
        }

        if (!Array.isArray(contentArray) || contentArray.length === 0) {
            container.innerHTML = '<div class="empty-state"><i class="fas fa-inbox fa-3x"></i><p>Nenhum registro encontrado no momento.</p></div>';
            return;
        }

        // Renderizar a lista baseada no tipo de endpoint
        container.innerHTML = `<ul class="data-list">` + contentArray.map(item => `
            <li class="data-item">
                <div class="data-icon"><i class="fas ${getIconForModel(endpoint)}"></i></div>
                <div class="data-details">
                    <h4>${extractName(item)}</h4>
                    <p>${extractDetails(item)}</p>
                </div>
            </li>
        `).join('') + `</ul>`;

    } catch (error) {
        container.innerHTML = `<div class="error-state"><i class="fas fa-exclamation-triangle fa-2x"></i><p>Não foi possível carregar os dados.</p><small>${error.message}</small></div>`;
    }
}

async function loadLotsForEventsModule() {
    const container = document.getElementById('moduleContent');
    container.innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> Carregando lotes...</div>';

    try {
        const res = await fetch(apiUrl('/lotes'));
        if (!res.ok) throw new Error('Falha ao buscar lotes');
        const lots = await res.json();

        if (!lots || lots.length === 0) {
            container.innerHTML = '<div class="empty-state"><p>Nenhum lote encontrado. Cadastre um lote primeiro.</p></div>';
            return;
        }

        container.innerHTML = lots.map(l => `
            <div class="card" style="margin-bottom:12px;">
                <div style="display:flex;justify-content:space-between;align-items:center;">
                    <div>
                        <h4>${l.lote || 'Lote'}</h4>
                        <small>${l.cultura || ''} — Status: ${l.status || ''}</small>
                    </div>
                    <div>
                        <button class="btn btn-primary" onclick="openLotEvents(${l.id})">Registrar eventos</button>
                    </div>
                </div>
                <p>Produção: ${l.producao ?? 'N/A'} — Custo: ${l.custo ?? 'N/A'} — Receita: ${l.receita ?? 'N/A'}</p>
            </div>
        `).join('');
    } catch (err) {
        container.innerHTML = `<div class="error-state"><p>${err.message}</p></div>`;
    }
}

// Specialized loaders for Lots and Cultures
async function loadLots() {
    const container = document.getElementById('lotsList');
    container.innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> Carregando lotes...</div>';
    try {
        const res = await fetch(apiUrl('/lotes'));
        if (!res.ok) throw new Error('Falha ao buscar lotes');
        const data = await res.json();
        renderLotsList(data);
    } catch (err) {
        container.innerHTML = `<div class="error-state"><p>${err.message}</p></div>`;
    }
}

function renderLotsList(lots) {
    const container = document.getElementById('lotsList');
    if (!lots || lots.length === 0) {
        container.innerHTML = '<div class="empty-state"><p>Nenhum lote encontrado.</p></div>';
        return;
    }
    container.innerHTML = lots.map(l => `
        <div class="card" style="margin-bottom:12px;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
                <div>
                    <h4>${l.lote || 'Lote'}</h4>
                    <small>${l.cultura || ''} — Status: ${l.status || ''}</small>
                </div>
                <div>
                    <button class="btn btn-secondary" onclick="prefillEditLot(${l.id})">Editar</button>
                    <button class="btn btn-info" onclick="openLotEvents(${l.id})">Eventos</button>
                    <button class="btn" style="background:#e74c3c;color:#fff;" onclick="deleteLot(${l.id})">Excluir</button>
                </div>
            </div>
            <p>Produção: ${l.producao ?? 'N/A'} — Custo: ${l.custo ?? 'N/A'} — Receita: ${l.receita ?? 'N/A'}</p>
        </div>
    `).join('');
}

async function handleCreateLot(e) {
    e.preventDefault();
    const payload = {
        nomeLote: document.getElementById('lotName').value,
        cultura: document.getElementById('lotCulture').value,
        producaoTotal: parseFloat(document.getElementById('lotProduction').value),
        custoTotal: parseFloat(document.getElementById('lotCost').value),
        precoVenda: parseFloat(document.getElementById('lotPrice').value)
    };
    try {
        const res = await fetch(apiUrl('/lotes'), {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error('Erro ao criar lote');
        document.getElementById('createLotForm').reset();
        showSuccess('Lote criado com sucesso');
        loadLots();
    } catch (err) {
        showError(err.message);
    }
}

async function deleteLot(id) {
    if (!confirm('Confirma exclusão do lote?')) return;
    try {
        const res = await fetch(apiUrl(`/lotes/${id}`), { method: 'DELETE' });
        if (!res.ok) throw new Error('Erro ao excluir lote');
        showSuccess('Lote removido');
        loadLots();
    } catch (err) {
        showError(err.message);
    }
}

async function prefillEditLot(id) {
    try {
        const res = await fetch(apiUrl(`/lotes/${id}`));
        if (!res.ok) throw new Error('Não foi possível carregar lote');
        const list = await res.json();
        const data = Array.isArray(list) ? list[0] : list;
        document.getElementById('lotName').value = data.lote || '';
        document.getElementById('lotCulture').value = data.cultura || '';
        document.getElementById('lotProduction').value = data.producao ?? '';
        document.getElementById('lotCost').value = data.custo ?? '';
        document.getElementById('lotPrice').value = data.precoVenda ?? data.receita ?? '';
        const form = document.getElementById('createLotForm');
        form.onsubmit = (ev) => handleUpdateLot(ev, id);
    } catch (err) {
        showError(err.message);
    }
}

async function handleUpdateLot(e, id) {
    e.preventDefault();
    const payload = {
        nomeLote: document.getElementById('lotName').value,
        cultura: document.getElementById('lotCulture').value,
        producaoTotal: parseFloat(document.getElementById('lotProduction').value),
        custoTotal: parseFloat(document.getElementById('lotCost').value),
        precoVenda: parseFloat(document.getElementById('lotPrice').value)
    };
    try {
        // Try PUT; if API doesn't support it, fallback to POST
        let res = await fetch(apiUrl(`/lotes/${id}`), {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (res.status === 404 || res.status === 405) {
            res = await fetch(apiUrl('/lotes'), {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload)
            });
        }
        if (!res.ok) throw new Error('Erro ao atualizar lote');
        document.getElementById('createLotForm').reset();
        // restore handler
        document.getElementById('createLotForm').onsubmit = (ev) => handleCreateLot(ev);
        showSuccess('Lote atualizado');
        loadLots();
    } catch (err) {
        showError(err.message);
    }
}

// Cultures
// Eventos (por lote)
let currentLotId = null;
let currentLotFinalized = false;

function setLotFinalizedUI(isFinalized) {
    currentLotFinalized = Boolean(isFinalized);

    const btn = document.getElementById('finalizeCultivationBtn');
    const info = document.getElementById('finalizeCultivationInfo');
    const form = document.getElementById('createEventForm');

    if (btn) {
        btn.disabled = currentLotFinalized;
        btn.textContent = currentLotFinalized ? 'Cultivo finalizado' : 'Finalizar cultivo';
    }

    if (info) {
        info.innerHTML = currentLotFinalized
            ? '<small>Este lote está finalizado. Novos eventos estão bloqueados.</small>'
            : '';
    }

    if (form) {
        form.querySelectorAll('input, button, textarea, select').forEach(el => {
            // Mantém o botão de finalizar fora do form
            if (el.id === 'finalizeCultivationBtn') return;
            el.disabled = currentLotFinalized;
        });
    }
}

function openLotEvents(lotId) {
    currentLotId = lotId;
    // carregar dados do lote (se possível)
    navigate('lot-detail');
    setLotFinalizedUI(false);
    document.getElementById('lotDetailTitle').textContent = `Lote #${lotId}`;
    document.getElementById('lotDetailMeta').textContent = `ID do lote: ${lotId}`;
    document.getElementById('eventsList').innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> Carregando eventos...</div>';
    hydrateLotDetailHeader(lotId);
    loadEvents(lotId);
}

async function hydrateLotDetailHeader(lotId) {
    try {
        const res = await fetch(apiUrl(`/lotes/${lotId}`));
        if (!res.ok) return;
        const list = await res.json();
        const lot = Array.isArray(list) ? list[0] : list;
        if (!lot) return;

        document.getElementById('lotDetailTitle').textContent = lot.lote || `Lote #${lotId}`;
        document.getElementById('lotDetailMeta').textContent = `${lot.cultura || ''} — Status: ${lot.status || ''}`;

        const status = String(lot.status || '').toLowerCase();
        setLotFinalizedUI(status === 'finalizado');
    } catch {
        // ignore
    }
}

async function handleFinalizeCultivation() {
    if (!currentLotId) return showError('Lote não selecionado');
    if (currentLotFinalized) return;

    const info = document.getElementById('finalizeCultivationInfo');
    if (info) info.innerHTML = '<small>Finalizando cultivo...</small>';

    try {
        // Carrega dados do lote para montar o LotRequest exigido pelo backend
        const lotRes = await fetch(apiUrl(`/lotes/${currentLotId}`));
        if (!lotRes.ok) throw new Error('Não foi possível carregar dados do lote');
        const list = await lotRes.json();
        const lot = Array.isArray(list) ? list[0] : list;
        if (!lot) throw new Error('Lote inválido');

        const totalProduction = Number(lot.producao ?? 0);
        const totalCost = Number(lot.custo ?? 0);
        const estimatedRevenue = Number(lot.receita ?? 0);
        const salePrice = totalProduction > 0 ? (estimatedRevenue / totalProduction) : 0;

        const payload = {
            nomeLote: lot.lote || `Lote #${currentLotId}`,
            cultura: lot.cultura || 'N/A',
            producaoTotal: isFinite(totalProduction) ? totalProduction : 0,
            custoTotal: isFinite(totalCost) ? totalCost : 0,
            precoVenda: isFinite(salePrice) ? salePrice : 0
        };

        const res = await fetch(apiUrl(`/lotes/${currentLotId}/finalizar-cultivo`), {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });

        if (!res.ok) {
            const msg = await res.text().catch(() => '');
            throw new Error(msg || 'Erro ao finalizar cultivo');
        }

        // Atualiza UI para bloquear novos eventos e recarrega lista
        setLotFinalizedUI(true);
        showSuccess('Cultivo finalizado. Lista de eventos gerada abaixo.');
        hydrateLotDetailHeader(currentLotId);
        await loadEvents(currentLotId);

        // Rola até a lista de eventos (para deixar claro que a lista foi gerada)
        const eventsList = document.getElementById('eventsList');
        if (eventsList) eventsList.scrollIntoView({ behavior: 'smooth', block: 'start' });

        if (info) info.innerHTML = '<small>Cultivo finalizado com sucesso.</small>';
    } catch (err) {
        if (info) info.innerHTML = '';
        showError(err.message);
    }
}

async function loadEvents(lotId) {
    try {
        const res = await fetch(apiUrl(`/lotes/${lotId}/eventos`));
        if (!res.ok) throw new Error('Falha ao carregar eventos');
        const data = await res.json();
        const events = Array.isArray(data) ? data : (data.content || data._embedded?.eventos || []);
        renderEventsList(events);
    } catch (err) {
        document.getElementById('eventsList').innerHTML = `<div class="error-state"><p>${err.message}</p></div>`;
    }
}

function toIsoLocalDateTimeAtStartOfDay(dateStr) {
    // Backend espera LocalDateTime; recebemos YYYY-MM-DD do input date.
    if (!dateStr) return null;
    return `${dateStr}T00:00:00`;
}

function formatIsoToDate(iso) {
    if (!iso) return '';
    if (typeof iso === 'string' && iso.includes('T')) return iso.split('T')[0];
    return String(iso);
}

function renderEventsList(events) {
    const container = document.getElementById('eventsList');
    if (!events || events.length === 0) {
        container.innerHTML = '<div class="empty-state"><p>Nenhum evento registrado para este lote.</p></div>';
        return;
    }
    container.innerHTML = events.map(ev => `
        <div class="card" style="margin-bottom:12px;">
            <div style="display:flex;justify-content:space-between;align-items:center;">
                <div>
                    <h4>${ev.tipoEvento || 'Evento'}</h4>
                    <small>
                        Data: ${formatIsoToDate(ev.dataPlantio)}
                        ${ev.dataColheitaEstimada ? ` — Colheita estimada: ${formatIsoToDate(ev.dataColheitaEstimada)}` : ''}
                    </small>
                </div>
            </div>
            <p>${ev.descricao || ''}</p>
        </div>
    `).join('');
}

async function handleCreateEvent(e) {
    e.preventDefault();
    if (!currentLotId) return showError('Lote não selecionado');
    if (currentLotFinalized) return showError('Este lote está finalizado e não aceita novos eventos');

    const plantingDate = toIsoLocalDateTimeAtStartOfDay(document.getElementById('eventDate').value);
    const estimatedHarvestDate = toIsoLocalDateTimeAtStartOfDay(document.getElementById('eventEstimatedHarvestDate').value);

    const payload = {
        tipoEvento: document.getElementById('eventType').value,
        descricao: document.getElementById('eventDescription').value || '',
        dataPlantio: plantingDate
    };
    if (estimatedHarvestDate) payload.dataColheitaEstimada = estimatedHarvestDate;

    try {
        const res = await fetch(apiUrl(`/lotes/${currentLotId}/eventos`), {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(payload)
        });
        if (!res.ok) throw new Error('Erro ao criar evento');
        document.getElementById('createEventForm').reset();
        showSuccess('Evento adicionado');
        loadEvents(currentLotId);
    } catch (err) {
        showError(err.message);
    }
}

// Utilidades para renderização dinâmica
function getIconForModel(endpoint) {
    const icons = {
        lots: 'fa-box',
        events: 'fa-calendar'
    };
    return icons[endpoint] || 'fa-hashtag';
}

function extractName(item) {
    if (item.lote) return item.lote;
    if (item.propertyName) return item.propertyName;
    if (item.lotNumber) return `Lote #${item.lotNumber}`;
    if (item.cultureName) return item.cultureName;
    if (item.certificationName) return item.certificationName;
    if (item.certificationCode) return `Cerificado #${item.certificationCode}`;
    return 'Item Sem Nome';
}

function extractDetails(item) {
    if (item.cultura || item.status) return `${item.cultura || ''} — Status: ${item.status || ''}`;
    if(item.totalArea) return `Área: ${item.totalArea}`;
    if(item.lotArea) return `Área: ${item.lotArea}`;
    if(item.descricao) return item.descricao;
    return `ID/Código: ${item.propertyId || item.lotId || item.cultureId || item.certificationTypeId || item.certificationCode || 'N/A'}`;
}

// ----------------------------------------------------
// UI Alert Functions
// ----------------------------------------------------
function showError(message) {
    createAlert(message, 'error');
}

function showSuccess(message) {
    createAlert(message, 'success');
}

function createAlert(message, type) {
    const container = document.getElementById('alert-container');
    const alert = document.createElement('div');
    alert.className = `alert alert-${type}`;
    alert.innerHTML = `
        <i class="fas ${type === 'success' ? 'fa-check-circle' : 'fa-times-circle'}"></i> 
        ${message}
    `;
    
    container.appendChild(alert);
    
    // Animate In
    setTimeout(() => alert.classList.add('show'), 10);
    
    // Remove after 4s
    setTimeout(() => {
        alert.classList.remove('show');
        setTimeout(() => alert.remove(), 300);
    }, 4000);
}
