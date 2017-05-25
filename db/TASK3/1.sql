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
