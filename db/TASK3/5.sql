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
