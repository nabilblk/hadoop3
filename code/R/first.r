# source('D:/KB/Hadoop - Training/Hadoop Materials/code/R/first.r')


#do mathematical operation in r conolse
16^2

#store a vector of country names
country = c("Brazil","China","India","Switzerland","USA")
le      = c (74,76,65,83,79)

#Sequence- pints 1,3,5,7
seqs = seq(1,100,2)

#vectors cant combine different data types. 
#ie country and life expectancy (le) cannot be combined into one vector
#However, a data frame (df) can do the same

df = data.frame(country,le)

#A df is also a matrix  and is aslo a table
#To add another column, say population 

pop    =c(199000,139000,1240000,7997,318000)
data2  =cbind(df,pop)

data2


#cbind binds (adds) another column
#rbind binds (rows) - adds another row
#to bind two tables, data

#***** The steps explained
#create vectors of columns using the 'c' function
#create a dataframe out of the vectors
#use cbind if you wish to add another colummn
#Finally merge two dfs to create rows

#Read a file

who = read.csv("D:/KB/Spark - EdX/The Analytics Edge/dataset/who.csv")
str(who)

summary(who)

# Subset - european subset

who_eu=subset(who,Region=="Europe")

# list of variables
ls()

write.csv("D:/KB/Spark - EdX/The Analytics Edge/dataset/who_result.csv")

# A factor is a variable in R. By default, R loads data as factors
# eg. > mons = c("March","April","January","November","January",
+ "September","October","September","November","August",
+ "January","November","November","February","May","August",
+ "July","December","August","August","September","November",
+ "February","April")
> mons = factor(mons)
> table(mons)
mons
    April    August  December  February   January      July
        2         4         1         2         3         1
    March       May  November   October September
        1         1         5         1         3
