import { Component } from 'react';
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
      tasks: tasks
      
    }
    
    log('constructor');
  }

  handleAutoCompleteFields(task){
    console.log(task);
    var t=this.refs['t'];
    console.log(t);
  }

  doUpdate(){
    console.log("update");
    // axios({
    //   method: 'put',
    //   url: 'http://192.168.0.103:8080/tasks/0',
    //   data: {
    //     number:0,
    //     tasks:this.state.tasks
    //   }
    // }).then(function(response){
    //   console.log(response);
    // });
  
  }

  handleOnSubmit(task){
    for(var i=0;i<this.state.tasks.length;i++){
      if(this.state.tasks[i].id==task.id){
        this.state.tasks[i]=task;
        this.forceUpdate();
        this.doUpdate();
        break;
      }
    }
  }

  render() {
    log('render');
    return (
      <View>
      <Consumer>
        {({isLoading, issue, tasks}) => (
          <View style={styles.content}>
            <ActivityIndicator animating={isLoading} style={styles.activityIndicator} size="large"/>
            {issue && <Text>{issueToText(issue)}</Text>}
            {tasks && tasks.map(task =><Task onSubmit={(task)=>{this.handleAutoCompleteFields(task)}} key={task.id} task={task}/>)}
            <TaskEdit ref="t" onSubmit={(task)=>this.handleOnSubmit(task) }></TaskEdit>
          </View>
          )}
      </Consumer>
        </View>
        
    );
  }

  componentDidMount(){
    console.log("did MOUNT");
  }
}
