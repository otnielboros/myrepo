import React, {Component} from 'react'
import {
    StyleSheet,
    View,
    Text,
    TextInput,
    TouchableOpacity
  } from 'react-native';


export class Login extends React.Component{
    constructor(props){
        super(props);
        this.state={
          username:'',
          password:''
        };
        console.log("Login - constructor");
    }

    // login(){
    //   //verifica credentiale+ salveaza token
    //   this.props.navigation.navigate("MainPage");
    //   console.log("Trying to sign in...");
    // }

    render(){
      console.log("Login - render");
      console.log(this.props);
      const {username,password} = this.state;
        return(
            <View style={styles.container}>
              {this.props.isLoading && <Text style={{fontSize:18}}> Loading...</Text>}
              {this.props.issue && <Text style={{fontSize:18}}> Error. </Text>}
                <Text style={{color:'#FFFFFF', fontSize:18, fontWeight:'600'}}>LOGIN</Text> 
                <TextInput style={styles.inputBox}  
                placeholder="Enter username"
                onChangeText={(username)=>{this.setState({username})}}
                underlineColorAndroid='rgba(0,0,0,0)'
                 placeholderTextColor='rgba(255,255,255,0.3)'/>

                 <TextInput placeholder="Enter password" style={styles.inputBox} 
                 onChangeText={(password)=>{this.setState({password})}}
                 placeholderTextColor='rgba(255,255,255,0.3)'
                 secureTextEntry
                 underlineColorAndroid='rgba(0,0,0,0)'/>

                 <TouchableOpacity style={styles.buttonContainer} onPress={()=>{this.props.onSubmit(username,password)}}>
                   <Text style={styles.buttonText}>Login</Text>
                  </TouchableOpacity> 
            </View>
        );
    }

    static getDerivedStateFromProps(){
        console.log("Login - get Derived State");
        return null;
    }

       
  componentDidMount () {
    console.log('Login - componentDidMount');
  }

  componentDidUpdate () {
    console.log('Login - componentDidUpdate');
  }

  componentWillUnmount () {
    console.log('Login - componentWillUnmount');
  }

}

export default Login;


const styles = StyleSheet.create({
    container: {
      backgroundColor:"#455a64",
      flex: 1,
      alignItems:'center',
      justifyContent:'center'
    },
    inputBox:{
      width: 300,
      backgroundColor: 'rgba(255,255,255,0.3)',
      borderRadius:25,
      paddingHorizontal: 16,
      fontSize:16,
      color:'#ffffff',
      marginVertical: 10,
      fontWeight:'600',
      height:40
    },
    buttonContainer:{
      backgroundColor:'#075552',
      paddingVertical:15,
      borderRadius:25,
      width: 300
    },
    buttonText:{
      textAlign:'center',
      color: "#ffffff",
      fontWeight:'600'
    }

  });