<template>
  <div class="app-container">
    <el-form label-width="120px">
      <el-form-item label="讲师名称">
        <el-input v-model="teacher.name" />
      </el-form-item>
      <el-form-item label="讲师排序">
        <el-input-number v-model="teacher.sort" controls-position="right" :min="0" />
      </el-form-item>
      <el-form-item label="讲师头衔">
        <el-select v-model="teacher.level" clearable placeholder="请选择">
          <!--
            数据类型一定要和取出的json中的一致，否则没法回填
            因此，这里value使用动态绑定的值，保证其数据类型是number
          -->
          <el-option :value="1" label="高级讲师" />
          <el-option :value="2" label="首席讲师" />
        </el-select>
      </el-form-item>
      <el-form-item label="讲师资历">
        <el-input v-model="teacher.career" />
      </el-form-item>
      <el-form-item label="讲师简介">
        <el-input v-model="teacher.intro" :rows="10" type="textarea" />
      </el-form-item>

      <!-- 讲师头像 -->
      <el-form-item label="讲师头像">
        <!-- 头衔缩略图 -->
        <pan-thumb :image="teacher.avatar" />
        <!-- 文件上传按钮 -->
        <el-button type="primary" icon="el-icon-upload" @click="imagecropperShow=true">更换头像</el-button>

        <!--
      v-show：是否显示上传组件
      :key：类似于id，如果一个页面多个图片上传控件，可以做区分
      :url：后台上传的url地址
      @close：关闭上传组件
        @crop-upload-success：上传成功后的回调-->
        <image-cropper
          v-show="imagecropperShow"
          :width="300"
          :height="300"
          :key="imagecropperKey"
          :url="BASE_API+'/admin/oss/file/upload'"
          field="file"
          @close="close"
          @crop-upload-success="cropSuccess"
        />
      </el-form-item>

      <el-form-item>
        <el-button :disabled="saveBtnDisabled" type="primary" @click="saveOrUpdate">保存</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { addTeahcer, getById, updateTeacher } from "@/api/teacher";
import ImageCropper from "@/components/ImageCropper";
import PanThumb from "@/components/PanThumb";
const defaultForm = {
  name: "",
  sort: 0,
  level: "",
  career: "",
  intro: "",
  avatar: "",
  id: ""
};
const OSSUrl = "http://127.0.0.1:8130";
export default {
  data() {
    return {
      teacher: defaultForm,
      saveBtnDisabled: false, // 保存按钮是否禁用,
      BASE_API: OSSUrl, // 接口API地址
      imagecropperShow: false, // 是否显示上传组件
      imagecropperKey: 0 // 上传组件id
    };
  },
  watch: {
    $route(to, from) {
      console.log("watch $route");
      this.init();
    }
  },
  components: {
    ImageCropper,
    PanThumb
  },
  created() {
    this.init();
  },
  methods: {
    init() {
      // 在页面加载之前， 判断路由里面是否有id值
      // 如果有id值，调用方法更加id查询
      // 从路由里面获取值
      if (this.$route.params && this.$route.params.id) {
        const id = this.$route.params.id;
        this.fetchDataById(id);
      } else {
        // 表单数据清空
        // 将defaultForm复制给teacher 实现清空的效果
        this.teacher = { ...defaultForm };
      }
    },
    // 实现添加和修改使用同一个表单
    // 预留做修改
    saveOrUpdate() {
      this.saveBtnDisabled = true;
      if (!this.teacher.id) {
        this.saveData();
      } else {
        this.updateDataById(this.teacher.id);
      }
    },
    saveData() {
      addTeahcer(this.teacher)
        .then(response => {
          if (response.code === 20000) {
            return this.$message({ type: "success", message: "保存成功！" });
          }
          this.$message({ type: "success", message: "保存成功！" });
        })
        .then(response => {
          this.$router.push({ path: "/edu/teacher" });
        });
    },
    fetchDataById(id) {
      getById(id).then(response => {
        this.teacher = response.data.teacher;
      });
    },
    updateDataById(id) {
      updateTeacher(id, this.teacher)
        .then(response => {
          return this.$message({ type: "success", message: "更新成功！" });
        })
        .then(response => {
          this.$router.push({ path: "/edu/teacher" });
        });
    },
    close() {
      this.imagecropperShow = false;
      // 上传失败后，重新打开上传组件时初始化组件，否则显示上一次的上传结果
      this.imagecropperKey = this.imagecropperKey + 1;
    },
    // 上传成功后的回调函数
    cropSuccess(data) {
      console.log(data);
      this.imagecropperShow = false;
      this.teacher.avatar = data.url;
      // 上传成功后，重新打开上传组件时初始化组件，否则显示上一次的上传结果
      this.imagecropperKey = this.imagecropperKey + 1;
    }
  }
};
</script>