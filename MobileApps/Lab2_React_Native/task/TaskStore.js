import { httpApiUrl, wsApiUrl,headers } from '../core/api';
import React, {Component,DeviceEventEmitter} from 'react';
import {Provider} from './context';
import {getLogger, issueToText} from "../core/utils";
import axios from 'axios'
import {Text,StyleSheet, View} from 'react-native'
import { AsyncStorage, NetInfo } from "react-native"
import {Brightness,Permissions} from 'expo'
import { Constants, DangerZone } from 'expo'
import { Subject , observable } from 'rxjs';

const log = getLogger('TaskStore');

class TaskStore extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoading: false,
      tasks:{} ,
      issue: null,
      isConnected:false,
      update: this.doUpdate,
      ws:null,
      bright: 0
    };
  }

  updateInStoredTasks=async (task)=>{
    var value = await AsyncStorage.getItem('tasks');
    value=JSON.parse(value);
    value[task.id]=task;  
     await this.storeTasks(value);
  };

  storeTasks = async (tasks)=>{
    try {
      await AsyncStorage.setItem('tasks',JSON.stringify(tasks));
      console.log("Stored");
      this.setState({tasks:tasks});
    } catch (error) {
      console.log(error);
    }
  }

  cacheWhenOnline= async (currentTasks,tasks)=>{
  
    Object.values(tasks).map(async (task)=>{
      var currentLocalTask=currentTasks[task.id];
      var upOrNot=false;
        if(!currentLocalTask){
          currentTasks[task.id]=task;
        }else{
          await this.checkEtag(currentLocalTask);
        }
      });
  }

  updateCall = async (task)=>{
    await axios({
      method: 'patch',
      url: `${httpApiUrl}/api/tasks/`+task.id,
      headers: {
        'Content-Type':'application/json',
        'Authorization':`Bearer ${this.props.tokenCode}`
      },
      data: JSON.stringify(task)
    })
    .then(json => {
      this.setState({ isLoading: false});
    })
    .catch(error => this.setState({ isLoading: false, issue: error }));
   }

   checkEtag=async (task)=>{
    await axios({
      method: 'get',
      url: `${httpApiUrl}/api/tasks/`+task.id,
      headers: {
        'Content-Type':'application/json',
        'Authorization':`Bearer ${this.props.tokenCode}`,
        'If-None-Match':`${task.etag}`
      }
    })
    .then(json => {
      task.etag=json.headers.etag;
      // this.updateInStoredTasks(task);
      // upOrNot=true;
      // console.log("UPORNOT");
      //fa update pe backend
      this.updateCall(task);
    })
    .catch(error => {
      // console.log("DONTUP");
      // upOrNot=false;
      //do nothing
     });
  }


  storeTasksAndDoCache = async (tasks) => {
    console.log("Store TASKS!!!!!!!");

    const currentTasksFromStorage=await AsyncStorage.getItem('tasks');
    
    var currentTasks=JSON.parse(currentTasksFromStorage);

    NetInfo.isConnected.fetch().then((isConnected)=>{
      if(isConnected){
        console.log("CACHEUIESC*************************************************");
        this.cacheWhenOnline(currentTasks,tasks);
      }else{
        console.log("No internet.");
      }
    })
      
    await this.storeTasks(currentTasks);
   }

 retrieveTasks = async () => {
     console.log("Retrieve TASKS");
     try {
         const value = await AsyncStorage.getItem('tasks');
         value=JSON.parse(value);
         if (value !== null) {
             this.setState({tasks: value,issue: null,})
         }
     } catch (error) {
         console.log("Error retrieve tasks");
     }
 };

  doUpdate= async (task)=>{
    task.etag="";
    NetInfo.isConnected.fetch().then((isConnected)=>{
      if(isConnected){
        this.updateCall(task);
      }else{
        console.log("No internet.");
      }
    });
  
   this.updateInStoredTasks(task);
  }

  loadTasks = () => {
    
    axios({
      method: 'get',
      url: `${httpApiUrl}/api/tasks`,
      headers: {
          'Content-Type':'application/json',
          'Authorization':`Bearer ${this.props.tokenCode}`
      }
    })
     .then(json => {this.storeTasksAndDoCache(json.data.tasks);this.setState({ isLoading: false, tasks: json.data.tasks })})
     .catch(error =>{this.retrieveTasks();});
  }

  handleConnectionChanged = (isConnected)=>{
     this.loadTasks();
  }

  sortFunction= (dictionary)=>{
    var keys=Object.keys(dictionary).sort((first,second)=>{
      console.log(dictionary[first]);
      return parseInt(dictionary[second.toString()].importance) - parseInt(dictionary[first.toString()].importance);
    });
    var newDictionary={};
    for(var i=0;i<keys.length;i++){
      console.log(keys[i]);
    }
    //console.log(dictionary);
  };

  componentDidMount(){
    Brightness.getSystemBrightnessAsync().then((bright)=>{
      console.log("Bright"+bright);
    });

    this.interval = setInterval(() =>{  Brightness.getSystemBrightnessAsync().then((brightness)=>{
      if(brightness!=this.state.bright){
        this.setState({bright: brightness});
        console.log("Birghtness modified");
        if(brightness>0.5){
            //this.setState({tasks:this.sortFunction(this.state.tasks)})
            //this.sortFunction(this.state.tasks);
        }
      }
    });}, 1000);

    NetInfo.addEventListener('connectionChange',this.handleConnectionChanged);
    this.connectWs();
    this.loadTasks();
  }

  componentWillUnmount() {
    this.disconnectWs();
    clearInterval(this.interval);
    log('componentWillUnmount');
  }



  deleteFromStorage= async (index)=>{
    var value = await AsyncStorage.getItem('tasks');
    value=JSON.parse(value);

    delete value[index];
     await this.storeTasks(value);
  }

  handleNotificationFromServer=async (notifyObjectJSON)=>{
   
    var notifyObject= JSON.parse(notifyObjectJSON);
  
    if(notifyObject.operation=="delete"){
      await this.deleteFromStorage(parseInt(notifyObject.task.id));
    }else{
      await this.updateInStoredTasks(notifyObject.task);
    }
  }

  connectWs = () => {
    
    const ws = new WebSocket(wsApiUrl);      
  
    ws.onopen = ()=> { 
      ws.send('Hello, from WebSocket client!');
    };
    
    ws.onmessage = (message)=> {
      this.handleNotificationFromServer(message.data);
    };
    this.ws=ws;
  };

  disconnectWs = () => {
   // this.ws.close();
  };

  render() {
   
    return (
      <Provider value={this.state} >
        {
          this.state.issue ? <View>
            <Text style={{fontSize:100}}>{issueToText(this.state.issue)}</Text></View> : this.props.children
       }
      </Provider>
    );
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center'
  },
});

export default TaskStore;