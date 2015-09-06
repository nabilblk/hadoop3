sqlline.bat -u "jdbc:drill:zk=local"

!set maxwidth 10000

//Movie Dataset

//Find all movies released in 1995

create table dfs.tmp.movies_1995 as
(select  cast (m.columns[0] as INT) movieId, m.columns[1] name  
from 
dfs.`D:\KB\Hadoop - Training\Hadoop Materials\data\Hive and Spark\Movie Data\ml-latest-small\movies2.csv` m
where m.columns[1] like '%(1995)%');


//Find the average rating of each movie
create table dfs.tmp.avg_ratings  as 
(select   cast(r.columns[1] as INT) movieId, avg (cast(r.columns[2] as FLOAT)) avg_rating  
from
dfs.`D:\KB\Hadoop - Training\Hadoop Materials\data\Hive and Spark\Movie Data\ml-latest-small\ratings2.csv` r
group by cast(r.columns[1] as INT));



  
select m.movieId, r.avg_rating, m.name 
from 
dfs.tmp.movies_1995 m, dfs.tmp.avg_ratings r
where m.movieId=r.movieId 
and
r.avg_rating>=4.0 ;



select count(*) 
from 
dfs.`D:\KB\Hadoop - Training\Hadoop Materials\data\Hive and Spark\Movie Data\ml-latest-small\movies.csv` ;