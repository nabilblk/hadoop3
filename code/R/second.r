source('D:/KB/Hadoop - Training/Hadoop Materials/code/R/first.r')

#List a variable in a dataframe
who$Under15


#Avg population under 15. Note variables are case sensitive
mean(who$Under15)

#avg population under 15 in South East Asia
who_asia=subset(who,Region=="South-East Asia")

mean(who_asia$Under15)

#avg population under 15 in India
who_india = subset(who,Country=="India")
mean(who_india$Under15)  #Ans. 29.43

#avg population over 60 in Eu

mean(who_eu$Over60) #19.77
mean(who_asia$Over60) #8.7
mean(who_india$Over60)#8.1


who_med=subset(who, Region=="Eastern Mediterranean")
mean(who_med$Under15)
mean(who_eu$Over60)

summary(who$Under15)

#Returns min 13.12, max 49.99, median 28.65 and mean 28.73
# Find the oldest population country. ie. where the population under15 avg is the lowest (=min=13.12)
which.min(who$Under15) # 86
who$Country[86] # Japan

#Country with the max population under 15 
who$Country[which.max(who$Under15)]

# Is it true that Japan also has the oldest population? ie max population over 60?
who$Country[which.max(who$Over60)] # Yes. 31.92 % of Japanese are over 60

# Is it true that Niger has the least population Over 60?
who$Country[which.min(who$Over60)] # No. UAE has only 0.81% of population under 60
#By contrast, India has only 8.1 % of population over 60 and roughly 29 % under 15





