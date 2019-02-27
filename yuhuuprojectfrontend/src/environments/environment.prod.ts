import {MockLoginService, ServerLoginService} from '../app/modules/login/services/login.service';

export const environment = {
  production: true,
  titleEnvironment: 'Prod Yuhuuuu',
  imageTestLink: 'https://images.pexels.com/photos/1539225/pexels-photo-1539225.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260',
  environmentTestText: 'Prod is on',
  loginService: ServerLoginService
};
