/*1. Develop a query to calculate the number of news, written by each author, the 
average number of comments per news for a current author and the most popular tag, 
referred to author news. All this information must be output in one single result set. 
Based on these query create a custom DB view.*/
create view author_view as
select na.NA_NAME "author name", 
count(nta.NEWS_ID) "news count",
trunc(nvl(avg(comments_info.comments_count), 0), 3) "average number of comments",
tag_info.popular_tag "most popular tag"
  from NEWS_AUTHORS na 
    left join NEWS_TO_AUTHORS nta on nta.NA_ID = na.NA_ID
    left join (
      select subnn.NEWS_ID news_id, count(subnc.NC_ID) comments_count 
        from  NEWS subnn
          left join NEWS_COMMENTS subnc on subnn.NEWS_ID = subnc.NC_NEWS_ID
              group by subnn.NEWS_ID
    ) comments_info on comments_info.news_id = nta.NEWS_ID
    left join (
      select author_id, min(popular_tags) popular_tag from (
        select first_sub.author_id author_id, first_sub.tag_name popular_tags
          from (
                (select subnta.NA_ID author_id, subnt.NT_NAME tag_name, count(subntt.NEWS_ID) tag_count
                  from NEWS_TO_AUTHORS subnta 
                    left join NEWS subnn on subnta.NEWS_ID = subnn.NEWS_ID 
                    left join NEWS_TO_TAGS subntt on subntt.NEWS_ID = subnn.NEWS_ID
                    left join NEWS_TAGS subnt on subnt.NT_ID = subntt.NT_ID
                      group by subnta.NA_ID, subnt.NT_NAME
                        order by author_id, count(subntt.NEWS_ID) DESC
                ) first_sub
                join
                (select author_id, max(tag_count) max_count from
                    (select subnta.NA_ID author_id, subnt.NT_NAME tag_name, count(subntt.NEWS_ID) tag_count
                      from NEWS_TO_AUTHORS subnta 
                        left join NEWS subnn on subnta.NEWS_ID = subnn.NEWS_ID 
                        left join NEWS_TO_TAGS subntt on subntt.NEWS_ID = subnn.NEWS_ID
                        left join NEWS_TAGS subnt on subnt.NT_ID = subntt.NT_ID
                          group by subnta.NA_ID, subnt.NT_NAME
                    )
                  group by author_id
                )  second_sub on first_sub.author_id = second_sub.author_id
        ) where first_sub.tag_count = second_sub.max_count
      ) group by author_id
    ) tag_info on tag_info.author_id = na.NA_ID
    group by na.NA_NAME, tag_info.popular_tag;

create view author_view_dense as
select na.NA_NAME "author name", 
count(nta.NEWS_ID) "news count",
trunc(nvl(avg(comments_info.comments_count), 0), 3) "average number of comments",
tag_info.popular_tag "most popular tag"
  from NEWS_AUTHORS na 
    left join NEWS_TO_AUTHORS nta on nta.NA_ID = na.NA_ID
    left join (
      select subnn.NEWS_ID news_id, count(subnc.NC_ID) comments_count 
        from  NEWS subnn
          left join NEWS_COMMENTS subnc on subnn.NEWS_ID = subnc.NC_NEWS_ID
              group by subnn.NEWS_ID
    ) comments_info on comments_info.news_id = nta.NEWS_ID
    left join (
      select author_id, tag_name popular_tag from
        (select subnta.NA_ID author_id, subnt.NT_NAME tag_name, row_number() 
        over (partition by subnta.NA_ID order by count(subntt.NEWS_ID) DESC) as rnk
          from NEWS_TO_AUTHORS subnta 
            left join NEWS subnn on subnta.NEWS_ID = subnn.NEWS_ID 
            left join NEWS_TO_TAGS subntt on subntt.NEWS_ID = subnn.NEWS_ID
            left join NEWS_TAGS subnt on subnt.NT_ID = subntt.NT_ID
              group by subnta.NA_ID, subnt.NT_NAME
        ) where rnk = 1
    ) tag_info on tag_info.author_id = na.NA_ID
    group by na.NA_NAME, tag_info.popular_tag;

/*2. Write SQL statement to select author names who wrote more than 3 000 characters, 
but the average number of characters per news is more than 500. 
Think about the shortest statement notation.*/
with news_table_length as
  (select nn.NEWS_ID news_id, (length(nn.NEWS_FULL_TEXT) + length(nn.NEWS_TITLE) + length(nn.NEWS_DESCRIPTION)) text_length from NEWS nn)
select na.NA_NAME
  from NEWS_AUTHORS na
    join NEWS_TO_AUTHORS nta on na.NA_ID = nta.NA_ID
    join NEWS nn on nn.NEWS_ID = nta.NEWS_ID
      group by na.NA_ID, na.NA_NAME
        having sum((select text_length from news_table_length where news_id = nn.NEWS_ID)) > 3000 
        and avg((select text_length from news_table_length where news_id = nn.NEWS_ID)) > 500;

select na.NA_NAME
  from NEWS_AUTHORS na
    join NEWS_TO_AUTHORS nta on na.NA_ID = nta.NA_ID
    join NEWS nn on nn.NEWS_ID = nta.NEWS_ID
      group by na.NA_ID, na.NA_NAME
        having sum(length(nn.NEWS_FULL_TEXT) + length(nn.NEWS_TITLE) + length(nn.NEWS_DESCRIPTION)) > 3000 
          and avg(length(nn.NEWS_FULL_TEXT) + length(nn.NEWS_TITLE) + length(nn.NEWS_DESCRIPTION)) > 500;

/*3. Create custom DB function that will return the list of all tags referred to
a current news, concatenated by specified separator character. Function must 
accept the news id and separator character as input parameters and return 
a single string as a result of tag values concatenation.*/
create or replace function TAG_LIST (NEWS_ID number, SEPARATOR varchar2) 
  return varchar2 AS
    RESULT_LIST varchar2(32000) := '';
    SEPARATOR_LENGTH number(2);
    cursor TAGS is select NT_NAME TAG_NAME from NEWS
      natural join NEWS_TO_TAGS
      natural join NEWS_TAGS
      where NEWS_ID = TAG_LIST.NEWS_ID;
begin
  if length(SEPARATOR) > 20 then
    raise_application_error(-20601, 'Separator length should not be greater then 20');
  end if;
  SEPARATOR_LENGTH := length(SEPARATOR);
  for TAGG in TAGS loop
    if length(RESULT_LIST) + length(TAGG.TAG_NAME) + SEPARATOR_LENGTH > 32000 then
      raise_application_error(-20600, 'Result list length should not be greater then 32000');
    end if;
    RESULT_LIST := RESULT_LIST || TAGG.TAG_NAME || SEPARATOR;
  end loop;
  RESULT_LIST := substr(RESULT_LIST, 1, length(RESULT_LIST) - length(SEPARATOR));
  RETURN RESULT_LIST;
end TAG_LIST;

select TAG_LIST(1, '::') from dual;


/*4. Develop single SQL statement that will return the list of all available 
news (news id, news title columns) and one more column that will display all
concatenated tags values, available for current news as a single string.*/
--a. Implement using previously developed custom function (#3)
select NEWS_ID, NEWS_TITLE, TAG_LIST(NEWS_ID, ', ') "list of tags"
  from NEWS;
--b. By using Oracle DB features
select NEWS_ID, NEWS_TITLE, LISTAGG(NT_NAME, ', ') within group(order by NT_NAME) "list of tags" from NEWS
  natural join NEWS_TO_TAGS
  natural join NEWS_TAGS
    group by NEWS_ID, NEWS_TITLE;

/*5. Make an author’s competition cross-map. Create a statement that will 
generate random authors distribution by competition pairs. Statement will
generate a list of author names pairs selected for a single round of 
tournament, displayed as two separate columns. Each author must be
presented in result set (in both columns) once only (the total number of 
authors must be even). Do not use custom functions during current implementation.*/
with random_names as
    (select rownum rwnm, NA_ID, NA_NAME from (select NA_ID, NA_NAME from NEWS_AUTHORS order by dbms_random.value)),
  half_author_count as
    (select count(1) / 2 half_cnt from NEWS_AUTHORS)
select first_authors.NA_NAME "first participant", second_authors.NA_NAME "second participant"
  from (select rownum rwnm, NA_ID, NA_NAME from random_names
          where random_names.rwnm <= (select half_cnt from half_author_count)) first_authors
    join (select rownum rwnm, NA_ID, NA_NAME from random_names
                where random_names.rwnm > (select half_cnt from half_author_count)) second_authors
      on first_authors.rwnm = second_authors.rwnm;
      
with random_names as
    (select rownum rwnm, NA_ID, NA_NAME from (select NA_ID, NA_NAME from NEWS_AUTHORS order by dbms_random.value))
select first_authors.NA_NAME "first participant", second_authors.NA_NAME "second participant"
  from (select rownum rwnm, NA_ID, NA_NAME from random_names
          where MOD(random_names.rwnm, 2) = 1) first_authors
    join (select rownum rwnm, NA_ID, NA_NAME from random_names
                where MOD(random_names.rwnm, 2) = 0) second_authors
      on first_authors.rwnm = second_authors.rwnm;

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

/*7. Prepare a SQL statement to output total/available space within each
tablespace. Calculate “Used %” column value.*/
select df.tablespace_name "Tablespace",
  df.totalspace "Total MB",
  (df.totalspace - tu.totalusedspace) "Available MB",
  round(100 * (tu.totalusedspace / df.totalspace), 2) "Used %"
from
  (select tablespace_name, round(sum(bytes) / 1048576, 3) TotalSpace from dba_data_files 
    group by tablespace_name
  ) df,
  (select round(sum(bytes) / 1048576, 3) totalusedspace, tablespace_name from dba_segments 
    group by tablespace_name) tu
      where df.tablespace_name = tu.tablespace_name and df.totalspace <> 0 ;

/*8. Prepare a SQL Statement to calculate each table size in your schema.*/
SELECT
   table_name, 
   round(sum(bytes) / 1024, 3) "Size in KB"
FROM
(SELECT segment_name table_name, bytes
 FROM user_segments
 WHERE segment_type IN ('TABLE', 'TABLE PARTITION', 'TABLE SUBPARTITION')
)
GROUP BY table_name
ORDER BY SUM(bytes) desc;
