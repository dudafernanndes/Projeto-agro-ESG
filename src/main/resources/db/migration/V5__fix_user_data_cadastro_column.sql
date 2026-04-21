-- V5__fix_user_data_cadastro_column.sql
-- Ensure USUARIOS_VITS has column DATA_CADASTRO (without VITS_ prefix)

DECLARE
  v_count NUMBER := 0;
BEGIN
  -- If VITS_DATA_CADASTRO exists and DATA_CADASTRO does not, rename it
  SELECT COUNT(*) INTO v_count
  FROM user_tab_cols
  WHERE table_name = 'USUARIOS_VITS'
    AND column_name = 'VITS_DATA_CADASTRO';

  IF v_count > 0 THEN
    SELECT COUNT(*) INTO v_count
    FROM user_tab_cols
    WHERE table_name = 'USUARIOS_VITS'
      AND column_name = 'DATA_CADASTRO';

    IF v_count = 0 THEN
      EXECUTE IMMEDIATE 'ALTER TABLE USUARIOS_VITS RENAME COLUMN VITS_DATA_CADASTRO TO DATA_CADASTRO';
    END IF;
  END IF;

  -- If DATA_CADASTRO still does not exist, add it with default
  SELECT COUNT(*) INTO v_count
  FROM user_tab_cols
  WHERE table_name = 'USUARIOS_VITS'
    AND column_name = 'DATA_CADASTRO';

  IF v_count = 0 THEN
    EXECUTE IMMEDIATE 'ALTER TABLE USUARIOS_VITS ADD (DATA_CADASTRO TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL)';
  END IF;
END;
/
