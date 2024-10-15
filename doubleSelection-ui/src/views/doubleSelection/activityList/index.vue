<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="活动名称" prop="name">
                <el-input v-model="queryParams.name" placeholder="请输入活动名称" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <!-- <el-date-picker v-model="time" type="daterange" range-separator="至" start-placeholder="开始时间"
                    end-placeholder="结束时间" value-format="yyyy-MM-dd">
                </el-date-picker> -->
            </el-form-item>

            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['doubleSelection:activity:add']">新增活动</el-button>
            </el-col>

            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="activityList" @selection-change="handleSelectionChange"
            :formatter="datefor">
            <el-table-column label="活动名称" align="center" prop="name" />
            <el-table-column label="活动详情" align="center" prop="description" />
            <el-table-column label="开始时间" align="center" prop="createTime" />
            <el-table-column label="截止日期" align="center" prop="endTime" />


        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改学生对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="活动名称" prop="name">
                    <el-input v-model="form.name" placeholder="请输入活动名称" />
                </el-form-item>
                <el-form-item label="活动详情" prop="description">
                    <el-input v-model="form.description" placeholder="请输入活动详情" />
                </el-form-item>

                <el-form-item label="开始时间" prop="createTime">
                    <el-date-picker v-model="form.createTime" type="date" value-format="yyyy-MM-dd" placeholder="选择开始时间"
                        style="width: 380px;">
                    </el-date-picker>
                </el-form-item>
                <el-form-item label="结束时间" prop="endTime">
                    <el-date-picker v-model="form.endTime" type="date" value-format="yyyy-MM-dd" placeholder="选择结束时间"
                        style="width: 380px;">
                    </el-date-picker>
                </el-form-item>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { listStudent, getStudent, delStudent, addStudent, updateStudent } from "@/api/doubleSelection/student";
import { addActivity, listActivity } from "../../../api/doubleSelection/currentActivity";
export default {
    name: "activityList",
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
            // 非多个禁用
            multiple: true,
            // 显示搜索条件
            showSearch: true,
            // 总条数
            total: 0,
            // 学生表格数据
            activityList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            time: [],
            // 查询参数
            queryParams: {
                page: 1,
                pageSize: 10,
                name: null,
                createTime: null,
                endTime: null
                // name: null,
                // major: null,
                // interests: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                name: [
                    { required: true, message: "活动名称不能为空", trigger: "blur" }
                ],
                createTime: [
                    { required: true, message: "开始时间不能为空", trigger: "blur" }
                ],
                endTime: [
                    { required: true, message: "结束时间不能为空", trigger: "blur" }
                ],


            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询学生列表 */
        getList() {
            this.loading = true;
            // console.log(this.time);
            // console.log(this.time);

            listActivity(this.queryParams).then(response => {
                // console.log(response);

                this.activityList = response.records
                this.total = response.total
                this.loading = false
            })
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                // studentId: null,
                description: null,
                name: null,
                createTime: null,
                endTime: null
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.page = 1;
            // console.log(this.time)
            // console.log(this.time.length)
            // console.log(this.queryParams)
            if (this.time.length !== 0) {
                this.queryParams.createTime = this.time[0]
                this.queryParams.endTime = this.time[1]
            }
            this.getList();
        },
        /** 重置按钮操作 */
        resetQuery() {
            this.resetForm("queryForm");
            this.handleQuery();
        },
        // 多选框选中数据
        handleSelectionChange(selection) {
            this.ids = selection.map(item => item.studentId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加活动";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const studentId = row.studentId || this.ids
            getStudent(studentId).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改学生";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.id != null) {
                        updateStudent(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addActivity(this.form).then(response => {
                            this.$modal.msgSuccess("新增成功");
                            this.open = false;
                            this.getList();
                        });
                    }
                }
            });
        },
        /** 删除按钮操作 */
        handleDelete(row) {
            const studentIds = row.studentId || this.ids;
            this.$modal.confirm('是否确认删除学生编号为"' + studentIds + '"的数据项？').then(function () {
                return delStudent(studentIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('doubleSelection/student/export', {
                ...this.queryParams
            }, `student_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>