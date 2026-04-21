// API Configuration
const API_BASE_URL = 'http://localhost:8080';
let authToken = localStorage.getItem('authToken');
let currentUser = JSON.parse(localStorage.getItem('currentUser')) || null;

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
}

function openModule(moduleName) {
    navigate('module');
    
    const titles = {
        properties: 'Gerenciar Propriedades',
        lots: 'Gerenciar Lotes',
        cultures: 'Gerenciar Culturas',
        certifications: 'Certificações e Selos'
    };

    document.getElementById('moduleTitle').textContent = titles[moduleName] || 'Módulo';
    document.getElementById('moduleContent').innerHTML = '<div class="loading-spinner"><i class="fas fa-spinner fa-spin"></i> Carregando dados...</div>';

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
        const response = await fetch(`${API_BASE_URL}/api/auth/login`, {
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
    const password = document.getElementById('registerPassword').value;

    try {
        const response = await fetch(`${API_BASE_URL}/api/auth/register`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userName: name, email, password })
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
    
    try {
        const response = await fetch(`${API_BASE_URL}/${endpoint}`, {
            headers: { 'Authorization': `Bearer ${authToken}` }
        });

        if (!response.ok) {
            if(response.status === 404) {
                 throw new Error('Recurso não encontrado (Backend pode não ter a rota ' + endpoint + ' implementada).');
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

// Utilidades para renderização dinâmica
function getIconForModel(endpoint) {
    const icons = {
        properties: 'fa-map',
        lots: 'fa-box',
        cultures: 'fa-leaf',
        certifications: 'fa-certificate'
    };
    return icons[endpoint] || 'fa-hashtag';
}

function extractName(item) {
    if (item.propertyName) return item.propertyName;
    if (item.lotNumber) return `Lote #${item.lotNumber}`;
    if (item.cultureName) return item.cultureName;
    if (item.certificationName) return item.certificationName;
    if (item.certificationCode) return `Cerificado #${item.certificationCode}`;
    return 'Item Sem Nome';
}

function extractDetails(item) {
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
