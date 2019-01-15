import React, { Component } from 'react';
import ReactDOM from 'react'
import {createStackNavigator} from 'react-navigation'
import Main from './login/Main'

import {
  StyleSheet,
  View
} from 'react-native';

import Login from './login/Login.js'
import { AuthProvider } from './login/AuthProvider';

export default class App extends React.Component {
  constructor(props){
    super(props)
    this.state={}
    console.log('App - constructor');
  }

  goToMainPage(){
    console.log("Hai ca sunt aici");
  }

  render() {
    return (
      <AuthProvider>
        <Main/>
      </AuthProvider>
    );
  }

  static getDerivedStateFromProps(){
    console.log('App - get Derived State');  
    return null;
  }

  componentDidMount () { 
   console.log("App - did mount");
  }

  componentDidUpdate () {
    console.log('App - componentDidUpdate');
  }

  componentWillUnmount () {
    console.log('App - componentWillUnmount');
  }


}


const RootStack = createStackNavigator({
  LoginPage: Login,
  MainPage: Main,
  initialRouteName: 'LoginPage',

});
