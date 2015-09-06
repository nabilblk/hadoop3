#right click on a folder and "git bash"
git init

# add a file
touch readme.txt
# git status  -- File will be untracked
# git add readme.txt


#git add . or git add *.html
git add code
git commit -m "commiting... "

# to display the log of commits
git log 

# to ignore certain files like *.log put them in gitignore and 
touch gitignore

git branch 1.0
git checkout 1.0

# go back to master
git checkout master

#to merge- be on the master and do the merge
git merge 1.0