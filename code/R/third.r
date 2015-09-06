source('D:/KB/Hadoop - Training/Hadoop Materials/code/R/second.r')

# FertilityRate to GNI correlation. 
# Notice that there is an inverse correlation between GNI & Fertility Rate
plot(who$GNI, who$FertilityRate)

# There are couple of countries with high income, high fertility rate. Which are those?
# Fertility > 2.5 and GNI>10,000 USD
outliers = subset(who, GNI> 10000 & FertilityRate>2.5 )

nrow(outliers) # 7 countries meet the criteria

# Direct correlation between population youth and FertilityRate (as expected)
plot(who$Under15,who$FertilityRate)

# Direct correlation between population over 60 and FertilityRate
plot(who$Over60,who$FertilityRate)
outliers [c("Country","GNI","FertilityRate")]

# Create Histograms

# of cellular subscriptions per 100 people. The bulk is between 50-150
hist(who$CellularSubscribers)

boxplot(who$LifeExpectancy ~ who$Region, xlab="Region", ylab="Life Expectancy", main="LifeExpectancy By Region")


# useful for representing factor variables
table (who$Region)

# Find the mean of population over 60 segmented by Region. Achieved by tapply (aka map)
# tapply breaks a vector into groups and applies a "map function"
tapply(who$Over60,who$Region, mean)


tapply(who$LiteracyRate,who$Region,min,na.rm=TRUE)

