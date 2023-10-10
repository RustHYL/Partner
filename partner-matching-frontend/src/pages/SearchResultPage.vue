<template>
  <van-card
      v-for="user in userList"
      :desc="user.profile"
      :title="`${user.username}\u3000\u3000 NO.${user.verifyCode}`"
      :thumb="user.avatarUrl"
      style="text-align: start"
  >
    <template #tags>
      <van-tag plain type="primary" v-for="tag in user.tags" style="margin-right: 8px; margin-top: 8px">{{ tag }}</van-tag>
    </template>
    <template #footer>
      <van-button size="mini">联系我</van-button>
    </template>
  </van-card>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {useRoute} from "vue-router";
import myAxios from "../plugins/myAxios.js"
import qs from 'qs';

const route = useRoute();

const {tags} = route.query;

onMounted(() => {
  myAxios.get('/user/search/tags', {
    params: {
      tagNameList: tags
    },
    paramsSerializer: params => {
      return qs.stringify(params, { indices: false })
    }
  })
  .then(function (response) {
    console.log('/user/search/tags success', response);
  })
  .catch(function (error) {
    console.error('/user/search/tags error', error);
  });
})


const mockUser = {
  id: 12366,
  username: 'xiaochou',
  userAccount: 'xiaochou123',
  avatarUrl: 'https://img1.baidu.com/it/u=3605832793,3252065221&fm=253&fmt=auto&app=120&f=JPEG?w=800&h=1422',
  gender: 0,
  phone: '17815700343',
  email: '1225250406@qq.com',
  profile: '2B NO.1',
  userStatus: 1,
  userRole: 0,
  verifyCode: '123456',
  createTime: new Date().toISOString(),
  tags: ['java','python','emo','learning'],
}

const userList = ref([mockUser]);



</script>

<style scoped>

</style>
