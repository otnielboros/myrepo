import React from 'react';
import {Text,View,TextInput,Button,TouchableOpacity,StyleSheet} from 'react-native';

export class TaskEdit extends React.Component{
    constructor(props){
        super(props);
        this.state={id:'',description:'',importance:''}
        console.log("TaskEdit - constructor***************^^^^^^^^");  
    }

    static getDerivedStateFromProps(){
        console.log("TaskEdit - get Derived State");
        
        return null;
    }

    handleOnPress(){

     this.props.updateFunction({id:this.state.id,description:this.state.description,importance:this.state.importance},this.props.taskList)
     .then((response)=>{
        console.log(response);
        this.props.doReload();
     }); 


     this.setState(
        {
        description:'',
        id:' ',
        importance:''
        }
      )
    }

    render(){
      console.log("TASK EDIT _RENDER");
      console.log(this.props);
      if((this.state.id!=this.props.task.id && !(this.state.id==' '))|| this.state.id==''){
      this.state={id:this.props.task.id.toString(),description:this.props.task.description,importance:this.props.task.importance.toString()};
      }else{
        if(this.state.id==' '){
        this.state.id='';
        }
      }
      const {id,description,importance} = this.state;
       return(
       <View>
        <TextInput  style={styles.inputBox}
          editable={false}
          style={{height: 40, width: 300}}
          placeholder="Id"
          value={this.state.id}
          //value={""+this.props.task.id}
          onChangeText={(id) => this.setState({id:id})}
        />
          <TextInput ref="id" style={styles.inputBox}
          style={{height: 40, width: 300}}
          placeholder="Enter description"
          //value={this.props.task.description}
          value={this.state.description}
          onChangeText={(description) => this.setState({description})}
        />

        <TextInput style={styles.inputBox}
          style={{height: 40, width: 300}}
          placeholder="Enter importance"
          value={this.state.importance}
          onChangeText={(importance) => this.setState({importance})}
        />
        <Button
          //onPress={()=>this.handleOnPress()}
          onPress={()=>this.props.updateFunction({id,description,importance})}
          title="Update"
        />

      </View>
      );
    }

    
  componentDidMount () {
    console.log('TaskEdit - componentDidMount');
  }

  componentDidUpdate () {
   
    console.log('TaskEdit - componentDidUpdate');
  }

  componentWillUnmount () {
    console.log('TaskEdit - componentWillUnmount');
  }
}

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
    fontSize:20,
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

export default TaskEdit;