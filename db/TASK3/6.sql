/*6. Create custom "LOGGING" DB table. The following corresponding columns 
must be created:
? Record insert date
? Referenced table name - table name where new record was inserted
? Description - the list of key-value pairs, separated by semicolon. 
Note: empty values and their column names must be omitted
Add required changes to your DB schema to populate current table columns each
time when new record was inserted to any DB table (in a scope of your schema).*/
CREATE TABLE LOGGING 
(
  INSERT_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
  TABLE_NAME VARCHAR2(20) NOT NULL,
  KEY_VALUE_LIST VARCHAR2(4000)
);

-- trigger to create LOGGER TRIGGERS
create or replace TRIGGER LOGGING_TRIGGER_CREATOR 
AFTER CREATE OR ALTER ON TASK3.SCHEMA
WHEN (SYS.DICTIONARY_OBJ_TYPE = 'TABLE')

DECLARE 
  create_query varchar2(10000) := '';
  cursor COLUMN_NAMES is select COLUMN_NAME COL_NAME from USER_TAB_COLS
          where TABLE_NAME = sys.dictionary_obj_name;

BEGIN

  if SYS.DICTIONARY_OBJ_TYPE = 'TABLE' then
    create_query := 'CREATE OR REPLACE TRIGGER ' || sys.dictionary_obj_name || '_LOGGER_TEST '      
     || 'BEFORE INSERT ON ' || sys.dictionary_obj_name
     || ' FOR EACH ROW 
    declare
      result_list varchar2(32000) :='''';
    begin
    ';
    
    for COLUMN_NAME in COLUMN_NAMES loop
      create_query := create_query || '
      if :new.' || COLUMN_NAME.COL_NAME || ' is not null then
        result_list := result_list || ''' || COLUMN_NAME.COL_NAME || ' = '' || :new.' || COLUMN_NAME.COL_NAME || ' || '';'';
      end if;
      ';
    end loop;
    
    create_query := create_query || 'insert into "LOGGING" (TABLE_NAME, KEY_VALUE_LIST) values (''' || sys.dictionary_obj_name || ''', result_list);
    end;';
  --
  --DBMS_OUTPUT.PUT_LINE(create_query);
  --
    EXECUTE IMMEDIATE create_query;
  end if;

END;

-- news authors logger
create or replace TRIGGER NEWS_AUTHORS_LOGGER 
BEFORE INSERT ON NEWS_AUTHORS
FOR EACH ROW
FOLLOWS NEWS_AUTHORS_BIR
DECLARE
  result_list varchar(200) := '';
BEGIN
  result_list := 'NA_ID = ' || :new.NA_ID || '; NA_NAME = ' || :new.NA_NAME || ';';
  insert into "LOGGING" (TABLE_NAME, KEY_VALUE_LIST) values ('NEWS_AUTHORS', result_list);
END;

--news comments logger
create or replace TRIGGER NEWS_COMMENTS_LOGGER 
BEFORE INSERT ON NEWS_COMMENTS 
FOR EACH ROW
FOLLOWS NEWS_COMMENTS_BIR
DECLARE
  result_list varchar(450) := '';
BEGIN
  result_list := 'NC_ID = ' || :new.NC_ID || '; NC_NEWS_ID = ' || :new.NC_NEWS_ID || '; NC_TEXT = ' || :new.NC_TEXT
    || '; NC_CREATION_TIME = ' || :new.NC_CREATION_TIME || ';';
  insert into "LOGGING" (TABLE_NAME, KEY_VALUE_LIST) values ('NEWS_COMMENTS', result_list);
END;

--news logger
create or replace TRIGGER NEWS_LOGGER 
BEFORE INSERT ON NEWS 
FOR EACH ROW
FOLLOWS NEWS_BIR
DECLARE
  result_list varchar(2500) := '';
BEGIN
  result_list := 'NEWS_ID = ' || :new.NEWS_ID || '; NEWS_TITLE = ' || :new.NEWS_TITLE || '; NEWS_CREATION_TIME = ' || :new.NEWS_CREATION_TIME
    || '; NEWS_DESCRIPTION = ' || :new.NEWS_DESCRIPTION || '; NEWS_FULL_TEXT = ' || :new.NEWS_FULL_TEXT || ';';
  if :new.NEWS_LAST_MODIFICATION_TIME is not null then
    result_list := result_list || ' NEWS_LAST_MODIFICATION_TIME = ' || :new.NEWS_LAST_MODIFICATION_TIME || ';';
  end if;
  insert into "LOGGING" (TABLE_NAME, KEY_VALUE_LIST) values ('NEWS', result_list);
END;

--news tags logger
create or replace TRIGGER NEWS_TAGS_LOGGER 
BEFORE INSERT ON NEWS_TAGS 
FOR EACH ROW
FOLLOWS NEWS_TAGS_BIR
DECLARE
  result_list varchar(100) := '';
BEGIN
  result_list := 'NT_ID = ' || :new.NT_ID || '; NT_NAME = ' || :new.NT_NAME || ';';
  insert into "LOGGING" (TABLE_NAME, KEY_VALUE_LIST) values ('NEWS_TAGS', result_list);
END;

--news to authors logger
create or replace TRIGGER NEWS_TO_AUTHORS_LOGGER 
BEFORE INSERT ON NEWS_TO_AUTHORS 
FOR EACH ROW

DECLARE
  result_list varchar(100) := '';
BEGIN
  result_list := 'NEWS_ID = ' || :new.NEWS_ID || '; NA_ID = ' || :new.NA_ID || ';';
  insert into "LOGGING" (TABLE_NAME, KEY_VALUE_LIST) values ('NEWS_TO_AUTHORS', result_list);
END;

--news to tags logger
create or replace TRIGGER NEWS_TO_TAGS_LOGGER 
BEFORE INSERT ON NEWS_TO_TAGS
FOR EACH ROW

DECLARE
  result_list varchar(100) := '';
BEGIN
  result_list := 'NEWS_ID = ' || :new.NEWS_ID || '; NT_ID = ' || :new.NT_ID || ';';
  insert into "LOGGING" (TABLE_NAME, KEY_VALUE_LIST) values ('NEWS_TO_TAGS', result_list);
END;
