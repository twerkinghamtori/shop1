<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isErrorPage="true"%> <!-- isErrorPage=true => exception 내장객체 전달됨. -->
<script>
	alert("${exception.message}")
	location.href="${exception.url}"
</script>