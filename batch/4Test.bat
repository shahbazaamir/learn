@echo off
echo for loop using array
set list=1 2 3 
(for %%a in (%list%) do ( 
   echo %%a 
))

echo for loop standard

FOR /L %%A IN (1,1,5) DO (
  ECHO %%A
)