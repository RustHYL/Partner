<template>
  <form action="/">
    <van-search
        v-model="searchText"
        show-action
        placeholder="请输入要搜索的标签"
        @search="onSearch"
        @cancel="onCancel"

    />
  </form>
</template>

<script setup>
import { ref } from 'vue';
import { showToast } from 'vant';

const searchText = ref('');
const onSearch = (val) => showToast(val);
const onCancel = () => showToast('取消');
</script>

<style scoped>

</style>