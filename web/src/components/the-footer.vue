<template>
  <a-layout-footer style="text-align: center">
     Design ©2023 Created by ChengQi {{user.name}}
  </a-layout-footer>
</template>


<script lang="ts">
import { defineComponent,computed} from 'vue';
import _default from "ant-design-vue/es/_util/store/Provider";
import store from "@/store";

export default defineComponent({
  name: 'the-footer',
  setup(){
    const user =computed(()=>
      store.state.user
    )
  return {
    user
  }
  };
});
</script>