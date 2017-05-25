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
