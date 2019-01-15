import React from 'react';
import {Text} from 'react-native';

export class Task extends React.Component{
    constructor(props){
        super(props);
        this.state={};
        console.log("Task - constructor");
    }

    static getDerivedStateFromProps(){
        console.log("Task - get Derived State");
        return null;
    }

    textPressed=()=>{
        this.props.onSubmit(this.props.task);
    };

    render(){
        return(
            <Text onPress={this.textPressed}>
                {"ID*:"+this.props.task.id+" Description:"+this.props.task.description+"->Importance:"+this.props.task.importance}
            </Text>
        );
    }

    
  componentDidMount () {
    console.log('Task - componentDidMount');
  }

  componentDidUpdate () {
    console.log('Task - componentDidUpdate');
  }

  componentWillUnmount () {
    console.log('Task - componentWillUnmount');
  }
}

export default Task;