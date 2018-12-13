lsnrctl status | grep -i dbname
sqlplus "/ as sysdba"
select name, open_mode from v$database;
select instance, name,startup_time from v_instance;
!date
