<script setup>
defineProps({
  tableData: Array,
});
</script>

<template>
  <table class="table_component">
    <thead class="header">
      <tr>
        <td v-for="(value, key) in tableData[0]" :key="key">{{ key }}</td>
      </tr>
    </thead>
    <tbody v-for="object in tableData" :key="object">
      <tr>
        <td v-for="element in object" :key="element">{{ element }}</td>
      </tr>
    </tbody>
  </table>
</template>

<style scoped>
.table_component {
  background-color: rgba(226, 226, 226, 0.938);
  text-align: center;
  border-collapse: collapse;
}

.table_component tr {
  border-bottom: 1px solid black;
}

.header {
  background-color: rgba(25, 25, 255, 0.527);
}

td {
  padding: 5px 30px 5px 30px;
}
</style>
