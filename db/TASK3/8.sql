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
