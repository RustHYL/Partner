<template>
  <van-form @submit="onSubmit">
    <van-cell-group inset>
      <van-field
          v-model="editUser.currentValue"
          :name="editUser.editKey"
          :label="editUser.editName"
          :placeholder="`请输入${editUser.editName}`"
      />
    </van-cell-group>
    <div style="margin: 16px;">
      <van-button round block type="primary" native-type="submit">
        提交
      </van-button>
    </div>
  </van-form>

</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ref} from "vue";
import myAxios from "../plugins/myAxios.ts";
import {showFailToast, showSuccessToast} from "vant";
import {getCurrentUser} from "../services/user.ts";

const route = useRoute();
const router = useRouter();
const editUser = ref({
  editKey: route.query.editKey,
  editName: route.query.editName,
  currentValue: route.query.currentValue
})


const onSubmit = async () => {
  //没返回id数据
  const currentUser = await getCurrentUser();
  if (!currentUser){
    return;
  }
  const res = await myAxios.post("/user/update", {
    'id': currentUser.id,
    [editUser.value.editKey as string]:editUser.value.currentValue
  })

  if (res.data > 0 && res.code === 0){
    router.back();
    showSuccessToast('更新成功');
  }else {
    showFailToast('更新失败');
  }
};

</script>

<style scoped>

</style>