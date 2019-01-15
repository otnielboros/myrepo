import React, { Component } from 'react';
import ReactDOM from 'react'

import {
  StyleSheet,
  View
} from 'react-native';

import { TaskList } from './Task/TaskList.js';
import TaskStore from './Task/TaskStore'
import {TaskEdit} from './Task/TaskEdit'


export default class App extends React.Component {
  constructor(props){
    super(props)
    this.state={}
    console.log('App - constructor');
  }

  

  render() {
    return (
    <View>
     <TaskStore>
      <TaskList />
    </TaskStore>
     </View>
    );
  }

  static getDerivedStateFromProps(){
    console.log('App - get Derived State');  
    return null;
  }

  componentDidMount () { 
   console.log("did mount");
  }

  componentDidUpdate () {
    console.log('App - componentDidUpdate');
  }

  componentWillUnmount () {
    console.log('App - componentWillUnmount');
  }


}


const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
  },
});
