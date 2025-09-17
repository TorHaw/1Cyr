target triple = "x86_64-pc-linux-gnu"

declare i32 @puts(ptr noundef)
declare i32 @scanf(ptr, ...)
declare i32 @printf(ptr, ...)
declare ptr @malloc(i64)
declare i64 @strlen(ptr noundef)

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @concat(ptr noundef %0, ptr noundef %1, ptr noundef %2) #0 {
  %4 = alloca ptr, align 8
  %5 = alloca ptr, align 8
  %6 = alloca ptr, align 8
  store ptr %0, ptr %4, align 8
  store ptr %1, ptr %5, align 8
  store ptr %2, ptr %6, align 8
  br label %7

7:                                                ; preds = %12, %3
  %8 = load ptr, ptr %4, align 8
  %9 = load i8, ptr %8, align 1
  %10 = sext i8 %9 to i32
  %11 = icmp ne i32 %10, 0
  br i1 %11, label %12, label %18

12:                                               ; preds = %7
  %13 = load ptr, ptr %4, align 8
  %14 = getelementptr inbounds i8, ptr %13, i32 1
  store ptr %14, ptr %4, align 8
  %15 = load i8, ptr %13, align 1
  %16 = load ptr, ptr %6, align 8
  %17 = getelementptr inbounds i8, ptr %16, i32 1
  store ptr %17, ptr %6, align 8
  store i8 %15, ptr %16, align 1
  br label %7, !llvm.loop !6

18:                                               ; preds = %7
  br label %19

19:                                               ; preds = %24, %18
  %20 = load ptr, ptr %5, align 8
  %21 = load i8, ptr %20, align 1
  %22 = sext i8 %21 to i32
  %23 = icmp ne i32 %22, 0
  br i1 %23, label %24, label %30

24:                                               ; preds = %19
  %25 = load ptr, ptr %5, align 8
  %26 = getelementptr inbounds i8, ptr %25, i32 1
  store ptr %26, ptr %5, align 8
  %27 = load i8, ptr %25, align 1
  %28 = load ptr, ptr %6, align 8
  %29 = getelementptr inbounds i8, ptr %28, i32 1
  store ptr %29, ptr %6, align 8
  store i8 %27, ptr %28, align 1
  br label %19, !llvm.loop !8

30:                                               ; preds = %19
  %31 = load ptr, ptr %6, align 8
  store i8 0, ptr %31, align 1
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @reverse(ptr noundef %0, ptr noundef %1) {
  %3 = alloca ptr, align 8
  %4 = alloca ptr, align 8
  %5 = alloca i32, align 4
  store ptr %0, ptr %3, align 8
  store ptr %1, ptr %4, align 8
  %6 = load ptr, ptr %3, align 8
  %7 = call i64 @strlen(ptr noundef %6)
  %8 = sub i64 %7, 1
  %9 = trunc i64 %8 to i32
  store i32 %9, ptr %5, align 4
  br label %10

10:                                               ; preds = %13, %2
  %11 = load i32, ptr %5, align 4
  %12 = icmp sge i32 %11, 0
  br i1 %12, label %13, label %22

13:                                               ; preds = %10
  %14 = load ptr, ptr %3, align 8
  %15 = load i32, ptr %5, align 4
  %16 = add nsw i32 %15, -1
  store i32 %16, ptr %5, align 4
  %17 = sext i32 %15 to i64
  %18 = getelementptr inbounds i8, ptr %14, i64 %17
  %19 = load i8, ptr %18, align 1
  %20 = load ptr, ptr %4, align 8
  %21 = getelementptr inbounds i8, ptr %20, i32 1
  store ptr %21, ptr %4, align 8
  store i8 %19, ptr %20, align 1
  br label %10, !llvm.loop !9

22:                                               ; preds = %10
  %23 = load ptr, ptr %4, align 8
  store i8 0, ptr %23, align 1
  ret void
}


define i32 @main() { 
	call i32 (ptr, ...) @printf(ptr @fmt, ptr @lit1)
	call i32 (ptr, ...) @printf(ptr @fmt, ptr @lit2)
	call i32 (ptr, ...) @printf(ptr @fmt, ptr @lit3)
	call i32 (ptr, ...) @printf(ptr @fmt, ptr @lit4)

	%reg0 = call ptr @malloc(i64 256)
	%reg1 = getelementptr inbounds [256 x i8], ptr %reg0, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr0, ptr %reg1)
	call i32 (ptr, ...) @printf(ptr @fmt, ptr %reg1)

	%reg2 = call ptr @malloc(i64 256)
	%reg3 = getelementptr inbounds [256 x i8], ptr %reg2, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr1, ptr %reg3)
	call i32 (ptr, ...) @printf(ptr @fmt, ptr %reg3)
	%reg4 = call ptr @malloc(i64 256)
	%out0 = getelementptr inbounds [256 x i8], ptr %reg4, i64 0, i64 0
	call void @concat(ptr noundef @lit5, ptr noundef @lit6, ptr noundef %out0)

	call i32 (ptr, ...) @printf(ptr @fmt, ptr %out0)

	%reg5 = call ptr @malloc(i64 256)
	%reg6 = getelementptr inbounds [256 x i8], ptr %reg5, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr2, ptr %reg6)

	%reg7 = call ptr @malloc(i64 256)
	%reg8 = getelementptr inbounds [256 x i8], ptr %reg7, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr3, ptr %reg8)

	%reg9 = call ptr @malloc(i64 256)
	%reg10 = getelementptr inbounds [256 x i8], ptr %reg9, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr4, ptr %reg10)
	%reg11 = call ptr @malloc(i64 256)
	%out1 = getelementptr inbounds [256 x i8], ptr %reg11, i64 0, i64 0
	call void @concat(ptr noundef %reg8, ptr noundef %reg10, ptr noundef %out1)

	%reg12 = call ptr @malloc(i64 256)
	%out2 = getelementptr inbounds [256 x i8], ptr %reg12, i64 0, i64 0
	call void @concat(ptr noundef %reg6, ptr noundef %out1, ptr noundef %out2)

	call i32 (ptr, ...) @printf(ptr @fmt, ptr %out2)
	%reg13 = call ptr @malloc(i64 256)
	%out3 = getelementptr inbounds [256 x i8], ptr %reg13, i64 0, i64 0
	call void @concat(ptr noundef @lit7, ptr noundef @lit8, ptr noundef %out3)

	%reg14 = call ptr @malloc(i64 256)
	%out4 = getelementptr inbounds [256 x i8], ptr %reg14, i64 0, i64 0
	call void @reverse(ptr noundef %out3,ptr noundef %out4)

	call i32 (ptr, ...) @printf(ptr @fmt, ptr %out4)

	%reg15 = call ptr @malloc(i64 256)
	%reg16 = getelementptr inbounds [256 x i8], ptr %reg15, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr5, ptr %reg16)
	%reg17 = call ptr @malloc(i64 256)
	%out5 = getelementptr inbounds [256 x i8], ptr %reg17, i64 0, i64 0
	call void @concat(ptr noundef @lit9, ptr noundef %reg16, ptr noundef %out5)

	call i32 (ptr, ...) @printf(ptr @fmt, ptr %out5)
	%reg18 = call ptr @malloc(i64 256)
	%out6 = getelementptr inbounds [256 x i8], ptr %reg18, i64 0, i64 0
	call void @reverse(ptr noundef @lit11,ptr noundef %out6)

	%reg19 = call ptr @malloc(i64 256)
	%out7 = getelementptr inbounds [256 x i8], ptr %reg19, i64 0, i64 0
	call void @concat(ptr noundef @lit10, ptr noundef %out6, ptr noundef %out7)

	call i32 (ptr, ...) @printf(ptr @fmt, ptr %out7)

	ret i32 0
}

@fmt = constant [4 x i8] c"%s
\00"
@lit1 = constant [6 x i8] c"aloha\00"
@lit2 = constant [7 x i8] c"	ALOHA\00"
@lit3 = constant [7 x i8] c"
AlOhA\00"
@lit4 = constant [8 x i8] c"Test 3:\00"
@ptr0 = constant [6 x i8] c"%255s\00"
@ptr1 = constant [6 x i8] c"%255s\00"
@lit5 = constant [8 x i8] c"Hello, \00"
@lit6 = constant [7 x i8] c"world!\00"
@ptr2 = constant [6 x i8] c"%255s\00"
@ptr3 = constant [6 x i8] c"%255s\00"
@ptr4 = constant [6 x i8] c"%255s\00"
@lit7 = constant [4 x i8] c"tah\00"
@lit8 = constant [5 x i8] c"looc\00"
@lit9 = constant [8 x i8] c"input: \00"
@ptr5 = constant [6 x i8] c"%255s\00"
@lit10 = constant [8 x i8] c"input: \00"
@lit11 = constant [6 x i8] c")ver(\00"

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{i32 8, !"PIC Level", i32 2}
!2 = !{i32 7, !"PIE Level", i32 2}
!3 = !{i32 7, !"uwtable", i32 2}
!4 = !{i32 7, !"frame-pointer", i32 2}
!5 = !{!"Ubuntu clang version 18.1.3 (1ubuntu1)"}
!6 = distinct !{!6, !7}
!7 = !{!"llvm.loop.mustprogress"}
!8 = distinct !{!8, !7}
!9 = distinct !{!9, !7}
