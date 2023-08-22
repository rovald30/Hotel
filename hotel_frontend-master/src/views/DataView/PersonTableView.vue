<script setup>
import Table from "@/components/_atoms/Table/Table.vue";
import DataMainView from "@/views/DataView/DataMainView.vue";
import { usePersonstore } from "@/stores/person";
import { storeToRefs } from "pinia";

const { responseData } = storeToRefs(usePersonstore());
</script>

<template>
  <DataMainView />
  <div class="container">
    <Table :tableData="responseData.data" />
  </div>
</template>

<style scoped>
.container {
  padding-top: 10%;
  padding-left: 25%;
}
</style>
