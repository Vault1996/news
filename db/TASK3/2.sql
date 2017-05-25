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
