
--inner join
SELECT E.ID, E.NAME, E.DEPARTMENT, I.ID AS INTERN_ID
FROM EMPLOYEE E
INNER JOIN INTERN I
ON E.ID = I.ID;

--outer join
--left
SELECT E.ID, E.NAME, E.DEPARTMENT, I.ID AS INTERN_ID
FROM EMPLOYEE E
LEFT JOIN INTERN I
ON E.ID = I.ID;


--right
SELECT E.ID, E.NAME, E.DEPARTMENT, I.ID AS INTERN_ID
FROM EMPLOYEE E
RIGHT JOIN INTERN I
ON E.ID = I.ID;

--full join
SELECT E.ID, E.NAME, E.DEPARTMENT, I.ID AS INTERN_ID
FROM EMPLOYEE E
FULL OUTER JOIN INTERN I
ON E.ID = I.ID;





SELECT MAX(SALARY) AS MAX_SALARY FROM EMPLOYEE;

--Max salary by department
SELECT DEPARTMENT, MAX(SALARY) AS MAX_SALARY
FROM EMPLOYEE
GROUP BY DEPARTMENT;

--Second highest salary overall
SELECT SALARY
FROM (
    SELECT SALARY, DENSE_RANK() OVER (ORDER BY SALARY DESC) AS RANK
    FROM EMPLOYEE
)
WHERE RANK = 2;


-- Second highest salary by department

SELECT * FROM (
    SELECT
        DEPARTMENT,
        NAME,
        SALARY,
        DENSE_RANK() OVER (PARTITION BY DEPARTMENT ORDER BY SALARY DESC) AS SALARY_RANK
    FROM EMPLOYEE
)
WHERE SALARY_RANK = 2;

-- cursor

BEGIN
   FOR emp_record IN (SELECT employee_id, first_name FROM employees WHERE department_id = 10)
   LOOP
      DBMS_OUTPUT.PUT_LINE(emp_record.employee_id || ' - ' || emp_record.first_name);
   END LOOP;
END;


DECLARE
   CURSOR emp_cursor IS SELECT employee_id, first_name FROM employees WHERE department_id = 10;
   v_id employees.employee_id%TYPE;
   v_name employees.first_name%TYPE;
BEGIN
   OPEN emp_cursor;
   LOOP
      FETCH emp_cursor INTO v_id, v_name;
      EXIT WHEN emp_cursor%NOTFOUND;
      DBMS_OUTPUT.PUT_LINE(v_id || ' - ' || v_name);
   END LOOP;
   CLOSE emp_cursor;
END;




select * from (
 select salary , dense_rank over ( order by salary desc) as rank from employee
) where rank=2

select * from (
select salary , dense_rank over ( partition by department order by salary desc ) as rank
) where rank =2
--old oracle
select a.* from tablea a , tableb b
where a.id = b.id(+)
 
select a.* from tablea a left join table b
on a.id(+) = b.id

 
begin
for cur in select * from employee
loop

end loop;
end;

declare
ac cursor as select * from employees

begin
open ac
fetch name into ac.name , age as ac.age
exit if not found
