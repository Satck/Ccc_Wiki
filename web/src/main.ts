import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import  Antd from 'ant-design-vue'
import  'ant-design-vue/dist/antd.css'
//需要哪些组件 就可以在这个里面进行使用

import  axios from 'axios'

// 这句话就是在改它的baseURL
axios.defaults.baseURL = process.env.VUE_APP_SERVER;


/**
 * axios拦截器
 */
axios.interceptors.request.use(function (config) {
    console.log('请求参数：', config);
    return config;
}, error => {
    return Promise.reject(error);
});
axios.interceptors.response.use(function (response) {
    console.log('返回结果：', response);
    return response;
}, error => {
    console.log('返回错误:',error)
    return Promise.reject(error);
});

import * as Icons from '@ant-design/icons-vue'
const app = createApp(App);
app.use(store).use(router).use(Antd).mount('#app');


const icons :any= Icons ;
for (const i in icons){
    app.component(i,icons[i]);
}
// process.env.xx x 读取环境变量
console.log('环境',process.env.NODE_ENV);