import type { ReactNode } from "react";

interface DashboardProps {
  children: ReactNode;
}
const Dashboard = (props: DashboardProps) => {
  const { children } = props;
  return (
    <>
      <div>Dashboard</div>
      {children}
    </>
  );
};

export default Dashboard;
