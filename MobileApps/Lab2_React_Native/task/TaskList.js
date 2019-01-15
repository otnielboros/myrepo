import React,{ Component } from 'react';
import { Text, View, ActivityIndicator, Platform} from 'react-native';
import styles from '../core/styles';
import { getLogger, issueToText } from '../core/utils';
import { Task } from "./Task";
import { Consumer } from './context';
import {TaskEdit} from './TaskEdit.js'


const log = getLogger('TaskList');
tasks=[]
export class TaskList extends Component {
  constructor(props) {
    super(props);
    this.state={
      taskList: {},
      task:{id:'',description:'',importance:''},
    }
    this.reload=this.reload.bind(this);
    log('constructor');
  }

  reload(){
    this.setState({task:{id:'',description:'',importance:''}});
  }

  handleAutoCompleteFields=(task)=>{
    console.log("AUTOCOMPLETE FIELD");
    this.setState({task:task});
  }

  handleOnSubmit(task){
    console.log("submit");
  }

  render() {
    log('RENDER TASK LIST');
    return (
      <View>
      <Consumer>
        {({isLoading, issue, tasks,update,bright}) => (
          <View>
            <ActivityIndicator animating={isLoading} style={styles.activityIndicator} size="large"/>
            {issue && <Text>{issueToText(issue)}</Text>}
            {bright<0.5 ? 
            tasks &&  Object.values(tasks).map((task)=><Task backgroundColor="#ffffff" onSubmit={(task)=>{this.handleAutoCompleteFields(task)}} key={task.id} task={task}/>) 
            : 
            tasks &&  Object.values(tasks).map((task)=><Task backgroundColor="#8b0000" onSubmit={(task)=>{this.handleAutoCompleteFields(task)}} key={task.id} task={task}/>) 
             }
             {<TaskEdit task={this.state.task} taskList={tasks} updateFunction={update}  doReload={this.reload}></TaskEdit> }
          </View>
        )}     
      </Consumer>
      </View>   
    );
  }
  
  componentDidMount(){
    console.log("did MOUNT TASK LIST");
  }
}

