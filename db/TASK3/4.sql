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
