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

