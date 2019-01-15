import React from 'react';
import {Text,View,TextInput,Button} from 'react-native';

export class TaskEdit extends React.Component{
    constructor(props){
        super(props);
        this.state={id:'',description:'',importance:''};
        console.log("TaskEdit - constructor");
    }

    static getDerivedStateFromProps(){
        console.log("TaskEdit - get Derived State");
        return null;
    }

    handleOnPress(){
        this.props.onSubmit({id:this.state.id,description:this.state.description,importance:this.state.importance});
        this.setState({id:'',description:'',importance:''});
      }

      handleComplete(task){
        this.setState({id:task.id.toString(),description:task.description,importance:task.importance.toString()});
      }

    render(){
        return(<View>
        <TextInput 
          editable={false}
          style={{height: 40, width: 300}}
          placeholder="Enter id"
          value={this.state.id}
          onChangeText={(id) => this.setState({id})}
        />
          <TextInput
          style={{height: 40, width: 300}}
          placeholder="Enter description"
          value={this.state.description}
          onChangeText={(description) => this.setState({description})}
        />

        <TextInput
          style={{height: 40, width: 300}}
          placeholder="Enter importance"
          value={this.state.importance}
          onChangeText={(importance) => this.setState({importance})}
        />
        <Button
          onPress={()=>this.handleOnPress()}
          title="Update"
        />
      </View>);
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

export default TaskEdit;