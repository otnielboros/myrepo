import React from 'react';
import {Text,View} from 'react-native';

export class Task extends React.Component{
    constructor(props){
        super(props);
        this.state={};
        
    }

    static getDerivedStateFromProps(){
       
        return null;
    }

    textPressed=()=>{
    
        var x=this.props.task;
        this.props.onSubmit(x);
    };

    render(){
        return(
            <View>
                {
                    this.props.task.importance==10
                     ? 
                    <Text style={{color:this.props.backgroundColor ,fontSize:16}} onPress={this.textPressed}>
                        {"ID*:"+this.props.task.id+" Description:"+this.props.task.description+"->Importance:"+this.props.task.importance}
                    </Text> 
                    : 
                    <Text style={{color:"#ffffff" ,fontSize:16}} onPress={this.textPressed}>
                        {"ID*:"+this.props.task.id+" Description:"+this.props.task.description+"->Importance:"+this.props.task.importance}
                    </Text>
                }
            
            </View>
        );
    }

    
  componentDidMount () {
   
  }

  componentDidUpdate () {
    
  }

  componentWillUnmount () {
    
  }
}

export default Task;