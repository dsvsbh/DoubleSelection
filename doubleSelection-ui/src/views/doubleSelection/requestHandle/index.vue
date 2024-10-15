<template>
    <div class="app-container">


        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['doubleSelection:student:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['doubleSelection:student:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete" v-hasPermi="['doubleSelection:student:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['doubleSelection:student:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="studentList" @selection-change="handleSelectionChange">
            <!-- <el-table-column type="selection" width="55" align="center" /> -->
            <!-- <el-table-column label="学生ID" align="center" prop="studentId" /> -->
            <el-table-column label="学生姓名" align="center" prop="name" />
            <el-table-column label="学生邮箱" align="center" prop="email" />
            <el-table-column label="专业" align="center" prop="major" />
            <el-table-column label="研究兴趣" align="center" prop="interests" />
            <el-table-column label="状态" align="center" prop="status">
                <template slot-scope="scope">
                    <span v-if="scope.row.status == 0">审核中</span>
                    <span v-if="scope.row.status == 1">已双选</span>
                    <span v-if="scope.row.status == 2">已拒绝</span>
                </template>

            </el-table-column>
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['doubleSelection:student:edit']">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['doubleSelection:student:remove']">删除</el-button>
                    <el-button v-if="scope.row.status == 0" size="small" type="primary"
                        @click="handleAccept(scope.row)">同意</el-button>
                    <el-button v-if="scope.row.status == 0" size="small" type="primary"
                        @click="handleReject(scope.row)">拒绝</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 添加或修改学生对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="学生姓名" prop="name">
                    <el-input v-model="form.name" placeholder="请输入学生姓名" />
                </el-form-item>
                <el-form-item label="学生邮箱" prop="email">
                    <el-input v-model="form.email" placeholder="请输入学生邮箱" />
                </el-form-item>
                <el-form-item label="专业" prop="major">
                    <el-input v-model="form.major" placeholder="请输入专业" />
                </el-form-item>
                <el-form-item label="研究兴趣" prop="interests">
                    <el-input v-model="form.interests" placeholder="请输入研究兴趣" />
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
import { acceptStudent, rejectStudent, selectedStudent } from "../../../api/doubleSelection/student";

export default {
    name: "Student",
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
            studentList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            status: "",
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                name: null,
                major: null,
                interests: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                name: [
                    { required: true, message: "学生姓名不能为空", trigger: "blur" }
                ],
                email: [
                    { required: true, message: "学生邮箱不能为空", trigger: "blur" }
                ],
                password: [
                    { required: true, message: "学生密码不能为空", trigger: "blur" }
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
            const pageNum = this.queryParams.pageNum
            const pageSize = this.queryParams.pageSize
            selectedStudent(pageNum, pageSize).then(response => {
                this.studentList = response.records;
                this.total = response.total;
                this.loading = false;
            });
        },
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                studentId: null,
                name: null,
                email: null,
                password: null,
                major: null,
                interests: null,
                createTime: null,
                updateTime: null
            };
            this.resetForm("form");
        },
        /** 搜索按钮操作 */
        handleQuery() {
            this.queryParams.pageNum = 1;
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
            this.title = "添加学生";
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
                    if (this.form.studentId != null) {
                        updateStudent(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addStudent(this.form).then(response => {
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
        },
        handleReject(row) {
            // this.getList()
            const studentId = row.studentId
            rejectStudent(studentId).then(response => {
                // console.log(response);
                this.$modal.msgSuccess('拒绝成功')

            })
            this.getList()
        },
        handleAccept(row) {
            const studentId = row.studentId
            acceptStudent(studentId).then(response => {
                // console.log(response);
                this.$modal.msgSuccess('双选成功')

            })
            this.getList()
        }
    }
};
</script>
