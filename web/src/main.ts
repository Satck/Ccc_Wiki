import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import  Antd from 'ant-design-vue'
import  'ant-design-vue/dist/antd.css'
//需要哪些组件 就可以在这个里面进行使用

createApp(App).use(store).use(router).use(Antd).mount('#app')
