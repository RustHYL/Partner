<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {onMounted, ref} from "vue";
import myAxios from "../plugins/myAxios";
import {showFailToast, showSuccessToast} from "vant";
import {TeamType} from "../models/team";

const router = useRouter();
const route = useRoute();
const id = Number(route.query.id);

const addTeamData = ref<TeamType>({});

onMounted(async () => {
  if (id <= 0){
    showFailToast("加载失败");
    return;
  }
  const res = await myAxios.get("/team/get", {
    params:{
      id,
    }
  })
  if (res?.code === 0 && res.data){
    addTeamData.value = res.data
  } else {
    showFailToast('添加失败');
  }
})

const showPicker = ref(false);

const minDate = new Date();

const onConfirm = ({ selectedValues }) => {
  addTeamData.value.expireTime = selectedValues.join('/');
  showPicker.value = false;
};

const onSubmit = async () => {
  const postData = {
    ...addTeamData.value,
    status: Number(addTeamData.value.status),
    expireTime: new Date(addTeamData.value.expireTime)
    // expireTime: timeFormat(new Date(addTeamData.value.expireTime))
  }
  console.log(addTeamData)
  const res = await myAxios.post("/team/update", postData)
  if (res?.code === 0 && res.data){
    showSuccessToast('更新成功');
    router.push({
      path: '/team',
      replace: true
    })
  } else {
    showFailToast('更新失败');
  }
}


</script>

<template>
  <div id="teamAddPage">
    <van-form @submit="onSubmit">
      <van-cell-group inset>
        <van-field
            v-model="addTeamData.name"
            name="name"
            label="队伍名"
            placeholder="请输入队伍名"
            :rules="[{ required: true, message: '请填写队伍名' }]"
        />
        <van-field
            v-model="addTeamData.description"
            rows="3"
            autosize
            label="队伍描述"
            type="textarea"
            placeholder="请输入描述"
        />

        <van-field
            v-model="addTeamData.expireTime"
            is-link
            readonly
            name="datePicker"
            label="过期时间"
            placeholder="点击选择过期时间"
            @click="showPicker = true"
        />
        <van-popup v-model:show="showPicker" position="bottom">
          <van-date-picker @confirm="onConfirm" @cancel="showPicker = false" title="请选择过期时间" type="datetime" :min-date="minDate"/>
        </van-popup>

        <van-field name="radio" label="队伍状态">
          <template #input>
            <van-radio-group v-model="addTeamData.status" direction="horizontal">
              <van-radio name="0">公开</van-radio>
              <van-radio name="1">私有</van-radio>
              <van-radio name="2">加密</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
            v-if="Number(addTeamData.status) === 2"
            v-model="addTeamData.password"
            type="password"
            name="password"
            label="密码"
            placeholder="请输入队伍密码"
            :rules="[{ required: true, message: '请填写队伍密码' }]"
        />
      </van-cell-group>
      <div style="margin: 16px;">
        <van-button round block type="primary" native-type="submit">
          登录
        </van-button>
      </div>
    </van-form>
  </div>
</template>

<style scoped>

</style>