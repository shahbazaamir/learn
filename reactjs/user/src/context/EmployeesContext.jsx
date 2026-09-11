import { createContext, useContext, useState } from 'react';
import { EMPLOYEES as INITIAL_EMPLOYEES } from '../data/employees';

const EmployeesContext = createContext(null);

// Provider — wrap the app with this to share employee state everywhere
export function EmployeesProvider({ children }) {
  const [employees, setEmployees] = useState(INITIAL_EMPLOYEES);

  const updateEmployee = (updatedEmployee) => {
    setEmployees((prev) =>
      prev.map((emp) => (emp.id === updatedEmployee.id ? updatedEmployee : emp))
    );
  };

  return (
    <EmployeesContext.Provider value={{ employees, updateEmployee }}>
      {children}
    </EmployeesContext.Provider>
  );
}

// Custom hook — any component can call useEmployees() to read/update
export function useEmployees() {
  return useContext(EmployeesContext);
}
