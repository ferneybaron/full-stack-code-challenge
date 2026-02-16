import type { ReactNode } from "react";

interface DashboardProps {
  children: ReactNode;
}
const Dashboard = (props: DashboardProps) => {
  const { children } = props;
  return <>{children}</>;
};

export default Dashboard;
