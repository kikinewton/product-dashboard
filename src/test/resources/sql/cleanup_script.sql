CREATE OR REPLACE FUNCTION truncate_all_tables_except_flyway()
        RETURNS VOID
AS
'
        DECLARE table_record RECORD;
        BEGIN
            FOR table_record IN
                SELECT * FROM information_schema.tables
                WHERE table_schema = ''public''
                AND table_type = ''BASE TABLE''
            LOOP
                IF table_record.table_name != ''flyway_schema_history''
                THEN EXECUTE format(''TRUNCATE TABLE %I CASCADE'', table_record.table_name);
                END IF;
            END LOOP;
        END;
'

        LANGUAGE plpgsql;

SELECT truncate_all_tables_except_flyway();