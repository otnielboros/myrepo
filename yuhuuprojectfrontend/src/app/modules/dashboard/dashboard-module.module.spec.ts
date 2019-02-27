import { DashboardModuleModule } from './dashboard-module.module';

describe('DashboardModuleModule', () => {
  let dashboardModuleModule: DashboardModuleModule;

  beforeEach(() => {
    dashboardModuleModule = new DashboardModuleModule();
  });

  it('should create an instance', () => {
    expect(dashboardModuleModule).toBeTruthy();
  });
});
