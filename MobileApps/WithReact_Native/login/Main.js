import React, { Component } from 'react';
import ReactDOM from 'react'
import {Button,TouchableOpacity} from 'react-native'
import {Consumer} from './context';

import {
  StyleSheet,
  View,Text
} from 'react-native';

import {TaskList} from '../task/TaskList'
import TaskStore from '../task/TaskStore'
import {TaskEdit} from '../task/TaskEdit'


export default class Main extends React.Component {
  constructor(props){
    super(props)
    this.state={}
    console.log('App - constructor');
  }

  render() {
    return (
      <Consumer>
        {({token,handleLogout})=>( 
      <View style={styles2.container}>
     <TaskStore tokenCode={token} >
      <TaskList/>
    </TaskStore>
    <TouchableOpacity  style={styles2.buttonContainer} onPress={()=>{handleLogout()}}>
      <Text style={styles2.buttonText}>LOGOUT</Text>
    </TouchableOpacity>
     </View> )}
     </Consumer>
    // {/* <View style={styles2.container}>
    //  <TaskStore tokenCode={this.props.token} >
    //   <TaskList/>
    // </TaskStore>
    // <TouchableOpacity  style={styles2.buttonContainer} onPress={()=>{this.props.logout()}}>
    //   <Text style={styles2.buttonText}>LOGOUT</Text>
    // </TouchableOpacity>
    //  </View> */}
  
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

const styles2 = StyleSheet.create({
  container: {
    backgroundColor:"#455a64",
    flex: 1,
    alignItems:'center',
    justifyContent:'center'
  },
  buttonContainer:{
    margin:20,
    backgroundColor:'#075552',
    paddingVertical:15,
    //borderRadius:25,
    width: 300
  },
  buttonText:{
    textAlign:'center',
    color: "#ffffff",
    fontWeight:'600'
  }

});


const styles = StyleSheet.create({
  container: {
    flex: 1,
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
  },
});
