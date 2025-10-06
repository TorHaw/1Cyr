; ModuleID = 'preamble.c'
source_filename = "preamble.c"
target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @reverse_string(ptr noundef %0, ptr noundef %1) #0 {
  %3 = alloca ptr, align 8
  %4 = alloca ptr, align 8
  %5 = alloca i32, align 4
  store ptr %0, ptr %3, align 8
  store ptr %1, ptr %4, align 8
  %6 = load ptr, ptr %3, align 8
  %7 = call i64 @strlen(ptr noundef %6) #2
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
  br label %10, !llvm.loop !6

22:                                               ; preds = %10
  %23 = load ptr, ptr %4, align 8
  store i8 0, ptr %23, align 1
  ret void
}

; Function Attrs: nounwind willreturn memory(read)
declare i64 @strlen(ptr noundef) #1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @reverse_bool(i32 noundef %0) #0 {
  %2 = alloca i32, align 4
  store i32 %0, ptr %2, align 4
  %3 = load i32, ptr %2, align 4
  %4 = icmp ne i32 %3, 0
  %5 = xor i1 %4, true
  %6 = zext i1 %5 to i32
  ret i32 %6
}

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
  br label %7, !llvm.loop !8

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
  br label %19, !llvm.loop !9

30:                                               ; preds = %19
  %31 = load ptr, ptr %6, align 8
  store i8 0, ptr %31, align 1
  ret void
}

attributes #0 = { noinline nounwind optnone uwtable "frame-pointer"="all" "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { nounwind willreturn memory(read) "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #2 = { nounwind willreturn memory(read) }

!llvm.module.flags = !{!0, !1, !2, !3, !4}
!llvm.ident = !{!5}

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


declare i32 @puts(ptr noundef)
declare i32 @scanf(ptr, ...)
declare i32 @printf(ptr, ...)
declare ptr @malloc(i64)
declare i32 @strcmp(i8* noundef, i8* noundef)
declare i8* @strstr(i8* noundef, i8* noundef)
declare i32 @sprintf(i8* noundef, i8* noundef, ...)


define i32 @main() { 
	call i32 (ptr, ...) @printf(ptr @fmt_str, ptr @lit1)

	%b0 = add i32 0, 0
	%b1 = add i32 0, 1
	call i32 (ptr, ...) @printf(ptr @fmt_str, ptr @lit2)

	%b2 = and i32 %b0, %b1
	call i32 (ptr, ...) @printf(ptr @fmt_int, i32 %b2)

	%b3 = or i32 %b0, %b1
	call i32 (ptr, ...) @printf(ptr @fmt_int, i32 %b3)

	call i32 (ptr, ...) @printf(ptr @fmt_str, ptr @lit5)

	%b4 = call i32 @strcmp(i8* noundef @lit3, i8* noundef @lit4)
	%b5 = icmp slt i32 %b4, 0
	%b6= zext i1 %b5 to i32
	call i32 (ptr, ...) @printf(ptr @fmt_int, i32 %b6)

	%reg0 = call ptr @strstr(i8* noundef @lit4, i8* noundef @lit3)
	%b7 = icmp ne ptr %reg0, null
	%b8= zext i1 %b7 to i32
	call i32 (ptr, ...) @printf(ptr @fmt_int, i32 %b8)

	%reg1 = call ptr @strstr(i8* noundef @lit4, i8* noundef @lit6)
	%b9 = icmp ne ptr %reg1, null
	%b10= zext i1 %b9 to i32
	call i32 (ptr, ...) @printf(ptr @fmt_int, i32 %b10)


	%reg2 = call ptr @malloc(i64 256)
	%reg3 = getelementptr inbounds [256 x i8], ptr %reg2, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr0, ptr %reg3)
	%reg4 = call ptr @malloc(i64 256)
	%out0 = getelementptr inbounds [256 x i8], ptr %reg4, i64 0, i64 0
	call void @concat(ptr noundef @lit7, ptr noundef %reg3, ptr noundef %out0)

	%reg5 = call ptr @malloc(i64 256)
	%out1 = getelementptr inbounds [256 x i8], ptr %reg5, i64 0, i64 0
	call void @concat(ptr noundef @lit8, ptr noundef %out0, ptr noundef %out1)

	%reg6 = call ptr @malloc(i64 256)
	%out2 = getelementptr inbounds [256 x i8], ptr %reg6, i64 0, i64 0
	call void @reverse_string(ptr noundef @lit9,ptr noundef %out2)

	%reg7 = call ptr @malloc(i64 256)
	%out3 = getelementptr inbounds [256 x i8], ptr %reg7, i64 0, i64 0
	call void @concat(ptr noundef %out1, ptr noundef %out2, ptr noundef %out3)

	call i32 (ptr, ...) @printf(ptr @fmt_str, ptr %out3)

	%reg8 = call ptr @malloc(i64 256)
	%out4 = getelementptr inbounds [256 x i8], ptr %reg8, i64 0, i64 0
	call void @reverse_string(ptr noundef @lit4,ptr noundef %out4)

	%reg9 = call ptr @malloc(i64 256)
	%out5 = getelementptr inbounds [256 x i8], ptr %reg9, i64 0, i64 0
	call void @reverse_string(ptr noundef %out4,ptr noundef %out5)

	%reg10 = call ptr @malloc(i64 256)
	%out6 = getelementptr inbounds [256 x i8], ptr %reg10, i64 0, i64 0
	call void @reverse_string(ptr noundef %out5,ptr noundef %out6)

	call i32 (ptr, ...) @printf(ptr @fmt_str, ptr %out6)


	%reg11 = call ptr @malloc(i64 256)
	%reg12 = getelementptr inbounds [256 x i8], ptr %reg11, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr1, ptr %reg12)

	%reg13 = call ptr @malloc(i64 256)
	%reg14 = getelementptr inbounds [256 x i8], ptr %reg13, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr2, ptr %reg14)
	%b11 = call i32 @strcmp(i8* noundef %reg12, i8* noundef %reg14)
	%b12 = icmp slt i32 %b11, 0
	%b13= zext i1 %b12 to i32
	%b14 = and i32 %b13, %b1
	call i32 (ptr, ...) @printf(ptr @fmt_int, i32 %b14)


	%reg15 = call ptr @malloc(i64 256)
	%reg16 = getelementptr inbounds [256 x i8], ptr %reg15, i64 0, i64 0
	call i32 (ptr, ...) @scanf(ptr @ptr3, ptr %reg16)
	%reg17 = call ptr @malloc(i64 256)
	%out7 = getelementptr inbounds [256 x i8], ptr %reg17, i64 0, i64 0
	call void @concat(ptr noundef @lit10, ptr noundef %reg16, ptr noundef %out7)

	%reg18 = call ptr @malloc(i64 256)
	%out8 = getelementptr inbounds [256 x i8], ptr %reg18, i64 0, i64 0
	call void @reverse_string(ptr noundef @lit11,ptr noundef %out8)

	%reg19 = call ptr @malloc(i64 256)
	%out9 = getelementptr inbounds [256 x i8], ptr %reg19, i64 0, i64 0
	call void @concat(ptr noundef %out7, ptr noundef %out8, ptr noundef %out9)

	%reg20 = call ptr @malloc(i64 256)
	%out10 = getelementptr inbounds [256 x i8], ptr %reg20, i64 0, i64 0
	call void @reverse_string(ptr noundef %out9,ptr noundef %out10)

	call i32 (ptr, ...) @printf(ptr @fmt_str, ptr %out10)


	ret i32 0
}

@fmt_str = constant [4 x i8] c"%s
\00"
@fmt_int = constant [4 x i8] c"%d
\00"
@int_to_str = constant [4 x i8] c"%d
\00"
@lit1 = constant [8 x i8] c"welcome\00"
@lit2 = constant [9 x i8] c"booleans\00"
@lit3 = constant [4 x i8] c"bye\00"
@lit4 = constant [6 x i8] c"hello\00"
@lit5 = constant [8 x i8] c"strings\00"
@lit6 = constant [5 x i8] c"ello\00"
@lit7 = constant [9 x i8] c"this is \00"
@ptr0 = constant [6 x i8] c"%255s\00"
@lit8 = constant [6 x i8] c"[hi] \00"
@lit9 = constant [3 x i8] c".!\00"
@ptr1 = constant [6 x i8] c"%255s\00"
@ptr2 = constant [6 x i8] c"%255s\00"
@lit10 = constant [5 x i8] c"dne \00"
@ptr3 = constant [6 x i8] c"%255s\00"
@lit11 = constant [5 x i8] c"cba \00"

