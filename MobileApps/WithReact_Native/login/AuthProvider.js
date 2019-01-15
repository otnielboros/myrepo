import React, {Component} from 'react'
import {Provider} from './context';
import {Login} from './Login'
import axios from 'axios'
import '../core/api'
import { apiUrl, httpApiUrl } from '../core/api';
import { AsyncStorage } from "react-native"

export class AuthProvider extends Component{
    constructor(props){
        super(props);
        this.state = {
            isLoading: false,
            issue: null,
            isAuthenticated: false,
            token: null,
          };
        console.log("Auth - Constructor");
    }

    storeToken = async (response) => {
       console.log("STORE!!");
       var access_token=response.data.access_token;
       console.log(access_token);
        try {
          await AsyncStorage.setItem('token',access_token);
          this.setState({isLoading:false,token:access_token});
        } catch (error) {
          console.log("Error saving token");
        }
      }

    retrieveToken = async () => {
        console.log("Retrieve");
        try {
            const value = await AsyncStorage.getItem('token');
            if (value !== null) {
                this.setState({token: value})
            }
        } catch (error) {
            console.log("Error retrieve token");
        }
    }

    deleteToken = async ()=>{
        try {
            await AsyncStorage.removeItem('token');
            this.setState({isLoading:false,token:null});
          } catch (error) {
            console.log("Error delete token");
          }
    }

    handleLogin=(username,password)=>{
        console.log("HandleLogin");
        this.setState({isLoading:false,token:null});

        var formData = new FormData();

         formData.append('grant_type','password');
         formData.append('username',username);
         formData.append('password',password);

        console.log(formData);
    
        axios({
            method: 'post',
            url: `${httpApiUrl}/oauth/token`,
            headers: {
                'Content-Type':'application/x-www-form-urlencoded',
                'Authorization':'Basic dGVzdGp3dGNsaWVudGlkOlhZN2ttem9OemwxMDA',
                'Preference-Applied':'outlook.allow-unsafe-html'
            },
            data:formData
          })
        .then(response => {this.storeToken(response)} )
    }

    componentDidMount(){
        this.retrieveToken();
        console.log("AuthProvider - Componend did mount");
    }

    handleLogout=()=>{
        this.deleteToken();
    }

    render(){
        const {issue,isLoading,token,isAuthenticated}=this.state;
        const {handleLogout}=this;
        // const { children } = this.props;
        // const childrenWithProps = React.Children.map(children, child =>
        //     React.cloneElement(child, { logout: this.handleLogout , token: this.state.token })
        //   );
        return (
            <Provider value={{token,handleLogout}}>
            { token && !isLoading
          ? this.props.children
          : <Login 
              isLoading={ isLoading}
              issue={ issue}
              onSubmit={ (username,password)=> this.handleLogin(username,password) }/>}
            </Provider>
          );
    }
}