
<<comment
# Schema Registry Test :
comment

:'
echo"This doesnt echo"
echo"Even this doesnt"
'

> file redirects stdout to file
1> file redirects stdout to file
2> file redirects stderr to file
&> file redirects stdout and stderr to file
> file 2>&1 redirects stdout and stderr to file
/dev/null is the null device it takes any input you want and throws it away. It can be used to suppress any output.
Note that > file 2>&1 is an older syntax which still works, &> file is neater, but would not have worked on older systems.

0 means stdin
1 means stdout(useful output)
2 means stderr(error message output)