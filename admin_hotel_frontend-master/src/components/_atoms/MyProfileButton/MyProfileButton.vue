<script setup>
import IconUserCircle from "@/components/icons/IconUserCircle.vue";

defineProps({
  username: String,
});
</script>

<template>
  <button data-testid="myprofile-button" class="login">
    {{ username }}
    <IconUserCircle data-testid="user-icon-circle" class="icon ml-4 right-0" />
  </button>
</template>

<style lang="scss" scoped>
.login {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px;
  width: 10rem;
  height: 2.5rem;
  background-color: #e7e7e7;
  font-size: 14px;
  font-weight: 500;
  color: #000;
  list-style: none;
  text-decoration: none;
  border: 1px solid black;

  &:hover {
    box-shadow: 0 0 7px rgba(0, 0, 0, 0.3);
  }
}

.icon {
  padding-left: 5px;
  width: 2rem;
}
</style>
