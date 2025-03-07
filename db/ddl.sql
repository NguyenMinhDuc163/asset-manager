USE [master]
GO
/****** Object:  Database [springboot_asset_dev]    Script Date: 28-Dec-20 11:20:13 PM ******/
CREATE DATABASE [springboot_asset_dev]
 
CREATE TABLE [dbo].[additional](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[in_process] [bit] NOT NULL,
	[time] [datetime2](7) NOT NULL,
	[organization_id] [bigint] NOT NULL,
	[user_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[additional_product]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[additional_product](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[price] [float] NOT NULL,
	[additional_id] [bigint] NOT NULL,
	[product_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[calculation_unit]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[calculation_unit](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[description] [ntext] NULL,
	[name] [varchar](10) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[campus]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[campus](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[campus_type] [varchar](255) NULL,
	[contact_email] [varchar](50) NOT NULL,
	[contact_phone] [varchar](15) NOT NULL,
	[description] [ntext] NULL,
	[location] [ntext] NULL,
	[map_url] [varchar](5000) NULL,
	[name] [varchar](15) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[category]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[category](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[description] [ntext] NULL,
	[name] [varchar](50) NOT NULL,
	[group_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[department]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[department](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[description] [ntext] NULL,
	[name] [varchar](30) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[groups]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[groups](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[code] [varchar](50) NOT NULL,
	[description] [ntext] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[inventory]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[inventory](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[end_time] [datetime2](7) NOT NULL,
	[in_check] [bit] NOT NULL,
	[start_time] [datetime2](7) NOT NULL,
	[time] [datetime2](7) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[inventory_material]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[inventory_material](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[inventory_id] [bigint] NOT NULL,
	[material_id] [bigint] NOT NULL,
	[user_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[liquidate]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[liquidate](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[done] [bit] NOT NULL,
	[time] [datetime2](7) NOT NULL,
	[user_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[liquidate_material]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[liquidate_material](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[liquidate_id] [bigint] NOT NULL,
	[material_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[material]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[material](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[credential_code] [varchar](100) NOT NULL,
	[is_have_include] [bit] NULL,
	[parent_code] [varchar](100) NULL,
	[status] [varchar](255) NOT NULL,
	[time_start_depreciation] [datetime2](7) NOT NULL,
	[additional_id] [bigint] NOT NULL,
	[current_place_id] [bigint] NULL,
	[product_id] [bigint] NOT NULL,
	[user_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[organization]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[organization](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[contact] [ntext] NULL,
	[name] [varchar](15) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[place]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[place](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[code] [varchar](20) NOT NULL,
	[description] [ntext] NULL,
	[direction] [ntext] NULL,
	[floor] [int] NULL,
	[name_specification] [ntext] NULL,
	[campus_id] [bigint] NOT NULL,
	[department_id] [bigint] NULL,
	[type_place_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[product]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[product](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[allocation_duration] [int] NOT NULL,
	[depreciation_rate] [float] NOT NULL,
	[description] [ntext] NULL,
	[name] [varchar](200) NOT NULL,
	[origin] [ntext] NULL,
	[time_allocation_type] [varchar](255) NOT NULL,
	[type] [varchar](255) NOT NULL,
	[calculation_unit] [bigint] NOT NULL,
	[category_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[roles]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[roles](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[role_name] [varchar](255) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[transfer_material]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[transfer_material](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[reason] [ntext] NULL,
	[time] [datetime2](7) NOT NULL,
	[material_id] [bigint] NOT NULL,
	[place_from_id] [bigint] NULL,
	[place_target_id] [bigint] NOT NULL,
	[user_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
/****** Object:  Table [dbo].[type_place]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[type_place](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[description] [ntext] NULL,
	[name] [varchar](20) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[user_role]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[user_role](
	[user_id] [bigint] NOT NULL,
	[role_id] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC,
	[role_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO
/****** Object:  Table [dbo].[users]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[users](
	[id] [bigint] IDENTITY(1,1) NOT NULL,
	[created_at] [datetime2](7) NOT NULL,
	[updated_at] [datetime2](7) NOT NULL,
	[active] [bit] NOT NULL,
	[email] [varchar](50) NOT NULL,
	[full_name] [ntext] NOT NULL,
	[password] [varchar](255) NOT NULL,
	[phone] [varchar](15) NOT NULL,
	[username] [varchar](50) NOT NULL,
	[department_id] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]

GO
SET ANSI_PADDING OFF
GO
SET IDENTITY_INSERT [dbo].[additional] ON 

INSERT [dbo].[additional] ([id], [created_at], [updated_at], [in_process], [time], [organization_id], [user_id]) VALUES (1, CAST(N'2020-12-24 09:43:34.8150000' AS DateTime2), CAST(N'2020-12-24 09:43:34.8150000' AS DateTime2), 1, CAST(N'2020-05-09 14:00:00.0000000' AS DateTime2), 1, 1)
SET IDENTITY_INSERT [dbo].[additional] OFF
SET IDENTITY_INSERT [dbo].[additional_product] ON 

INSERT [dbo].[additional_product] ([id], [created_at], [updated_at], [price], [additional_id], [product_id]) VALUES (1, CAST(N'2020-12-24 09:47:42.6010000' AS DateTime2), CAST(N'2020-12-24 09:47:42.6010000' AS DateTime2), 10000000, 1, 1)
INSERT [dbo].[additional_product] ([id], [created_at], [updated_at], [price], [additional_id], [product_id]) VALUES (2, CAST(N'2020-12-24 09:47:42.8700000' AS DateTime2), CAST(N'2020-12-24 09:47:42.8700000' AS DateTime2), 20550000, 1, 2)
INSERT [dbo].[additional_product] ([id], [created_at], [updated_at], [price], [additional_id], [product_id]) VALUES (3, CAST(N'2020-12-25 22:50:21.0170000' AS DateTime2), CAST(N'2020-12-25 22:50:21.0170000' AS DateTime2), 200000000, 1, 3)
SET IDENTITY_INSERT [dbo].[additional_product] OFF
SET IDENTITY_INSERT [dbo].[calculation_unit] ON 

INSERT [dbo].[calculation_unit] ([id], [created_at], [updated_at], [description], [name]) VALUES (1, CAST(N'2020-12-24 09:40:31.0210000' AS DateTime2), CAST(N'2020-12-24 09:40:31.0210000' AS DateTime2), N'Một cái', N'CAI')
SET IDENTITY_INSERT [dbo].[calculation_unit] OFF
SET IDENTITY_INSERT [dbo].[campus] ON 

INSERT [dbo].[campus] ([id], [created_at], [updated_at], [campus_type], [contact_email], [contact_phone], [description], [location], [map_url], [name]) VALUES (1, CAST(N'2020-12-24 09:40:05.3790000' AS DateTime2), CAST(N'2020-12-24 09:40:05.3790000' AS DateTime2), N'FACILITY', N'tanhanh@gmail.com', N'0903594837', N'Học Viện Công Nghệ Bưu Chính Viễn Thông Quận 9', N'Đường Man Thiện, phường Tăng Nhơn Phú A, Quận 9, HCM', N'no', N'HVCN-BCVT-Q9')
SET IDENTITY_INSERT [dbo].[campus] OFF
SET IDENTITY_INSERT [dbo].[category] ON 

INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (1, CAST(N'2020-11-28 14:33:17.0720000' AS DateTime2), CAST(N'2020-11-28 14:33:17.0720000' AS DateTime2), N'Nhà cửa Loại Kiên Cố', N'NCLKC', 1)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (2, CAST(N'2020-11-28 14:34:11.0060000' AS DateTime2), CAST(N'2020-11-28 14:34:11.0060000' AS DateTime2), N'Nhà cửa Loại Khác', N'NCLK', 1)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (3, CAST(N'2020-11-28 14:34:46.0100000' AS DateTime2), CAST(N'2020-11-28 14:34:46.0100000' AS DateTime2), N'Vật Kiến Trúc', N'VKT', 1)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (4, CAST(N'2020-11-28 14:35:06.4950000' AS DateTime2), CAST(N'2020-11-28 14:35:06.4950000' AS DateTime2), N'Máy Móc Thiết Bị - Động Lực', N'MMTB-DL', 2)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (5, CAST(N'2020-11-28 14:35:21.6260000' AS DateTime2), CAST(N'2020-11-28 14:35:21.6260000' AS DateTime2), N'Máy Móc Thiết Bị - Công Tác', N'MMTB-CT', 2)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (6, CAST(N'2020-11-28 14:35:38.0940000' AS DateTime2), CAST(N'2020-11-28 14:35:38.0940000' AS DateTime2), N'Dụng Cụ Làm Việc - Đo Lường Thí Nghiệm', N'DCLV-DLTN', 2)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (7, CAST(N'2020-11-28 14:36:07.3470000' AS DateTime2), CAST(N'2020-11-28 14:36:07.3470000' AS DateTime2), N'Phương Tiện Vận Tải Đường Bộ', N'PTVT-DB', 3)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (8, CAST(N'2020-11-28 14:36:29.3140000' AS DateTime2), CAST(N'2020-11-28 14:36:29.3140000' AS DateTime2), N'Máy Tính Xách Tay', N'MTXT', 4)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (9, CAST(N'2020-11-28 14:36:44.3210000' AS DateTime2), CAST(N'2020-11-28 14:36:44.3210000' AS DateTime2), N'Máy Chủ Server', N'MCSV', 4)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (10, CAST(N'2020-11-28 14:37:11.2650000' AS DateTime2), CAST(N'2020-11-28 14:37:11.2650000' AS DateTime2), N'Máy Chiếu', N'MC', 4)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (11, CAST(N'2020-11-28 14:37:25.1580000' AS DateTime2), CAST(N'2020-11-28 14:37:25.1580000' AS DateTime2), N'Máy Photocopy', N'MPTCP', 4)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (12, CAST(N'2020-11-28 14:37:37.1270000' AS DateTime2), CAST(N'2020-11-28 14:37:37.1270000' AS DateTime2), N'Máy Điều Hòa', N'MDH', 4)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (13, CAST(N'2020-11-28 14:37:55.5960000' AS DateTime2), CAST(N'2020-11-28 14:37:55.5960000' AS DateTime2), N'Các Phương Tiện Quản Lý Khác', N'CPTQLK', 4)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (14, CAST(N'2020-11-28 14:38:11.7870000' AS DateTime2), CAST(N'2020-11-28 14:38:11.7870000' AS DateTime2), N'Trạm Biến Áp', N'TBA', 5)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (15, CAST(N'2020-11-28 14:38:24.1840000' AS DateTime2), CAST(N'2020-11-28 14:38:24.1840000' AS DateTime2), N'Hệ Thống Khác', N'HTK', 5)
INSERT [dbo].[category] ([id], [created_at], [updated_at], [description], [name], [group_id]) VALUES (16, CAST(N'2020-11-28 14:38:41.1690000' AS DateTime2), CAST(N'2020-11-28 14:38:41.1690000' AS DateTime2), N'Đền Bù Đất', N'DBD', 6)
SET IDENTITY_INSERT [dbo].[category] OFF
SET IDENTITY_INSERT [dbo].[department] ON 

INSERT [dbo].[department] ([id], [created_at], [updated_at], [description], [name]) VALUES (1, CAST(N'2020-12-24 09:40:46.4070000' AS DateTime2), CAST(N'2020-12-24 09:40:46.4070000' AS DateTime2), N'Công Nghệ Thông Tin', N'CNTT')
SET IDENTITY_INSERT [dbo].[department] OFF
SET IDENTITY_INSERT [dbo].[groups] ON 

INSERT [dbo].[groups] ([id], [created_at], [updated_at], [code], [description]) VALUES (1, CAST(N'2020-11-28 14:30:44.6260000' AS DateTime2), CAST(N'2020-11-28 14:30:44.6260000' AS DateTime2), N'NC-VKT', N'Nhà Cửa - Vật Kiến Trúc')
INSERT [dbo].[groups] ([id], [created_at], [updated_at], [code], [description]) VALUES (2, CAST(N'2020-11-28 14:31:08.3430000' AS DateTime2), CAST(N'2020-11-28 14:31:08.3430000' AS DateTime2), N'MM-TB', N'Máy Móc - Thiết Bị')
INSERT [dbo].[groups] ([id], [created_at], [updated_at], [code], [description]) VALUES (3, CAST(N'2020-11-28 14:31:30.5340000' AS DateTime2), CAST(N'2020-11-28 14:31:30.5340000' AS DateTime2), N'TB-PTVT', N'Thiết Bị - Phương Tiện Vận Tải')
INSERT [dbo].[groups] ([id], [created_at], [updated_at], [code], [description]) VALUES (4, CAST(N'2020-11-28 14:31:51.3240000' AS DateTime2), CAST(N'2020-11-28 14:31:51.3240000' AS DateTime2), N'TB-DCQL', N'Thiết Bị - Dụng Cụ Quản Lý')
INSERT [dbo].[groups] ([id], [created_at], [updated_at], [code], [description]) VALUES (5, CAST(N'2020-11-28 14:32:07.0760000' AS DateTime2), CAST(N'2020-11-28 14:32:07.0760000' AS DateTime2), N'TSCDHHK', N'Tài Sản Cố Định Hữu Hình Khác')
INSERT [dbo].[groups] ([id], [created_at], [updated_at], [code], [description]) VALUES (6, CAST(N'2020-11-28 14:32:21.7430000' AS DateTime2), CAST(N'2020-11-28 14:32:21.7430000' AS DateTime2), N'TSCDVH', N'Tài Sản Cố Định Vô Hình')
SET IDENTITY_INSERT [dbo].[groups] OFF
SET IDENTITY_INSERT [dbo].[inventory] ON 

INSERT [dbo].[inventory] ([id], [created_at], [updated_at], [end_time], [in_check], [start_time], [time]) VALUES (1, CAST(N'2020-12-24 09:46:55.9280000' AS DateTime2), CAST(N'2020-12-24 09:46:55.9280000' AS DateTime2), CAST(N'2020-12-28 14:00:00.0000000' AS DateTime2), 1, CAST(N'2020-10-25 14:00:00.0000000' AS DateTime2), CAST(N'2020-12-29 14:00:00.0000000' AS DateTime2))
SET IDENTITY_INSERT [dbo].[inventory] OFF
SET IDENTITY_INSERT [dbo].[inventory_material] ON 

INSERT [dbo].[inventory_material] ([id], [created_at], [updated_at], [inventory_id], [material_id], [user_id]) VALUES (1, CAST(N'2020-12-24 10:11:44.7810000' AS DateTime2), CAST(N'2020-12-24 10:11:44.7810000' AS DateTime2), 1, 1, 1)
INSERT [dbo].[inventory_material] ([id], [created_at], [updated_at], [inventory_id], [material_id], [user_id]) VALUES (2, CAST(N'2020-12-25 23:02:53.6020000' AS DateTime2), CAST(N'2020-12-25 23:02:53.6020000' AS DateTime2), 1, 2, 3)
SET IDENTITY_INSERT [dbo].[inventory_material] OFF
SET IDENTITY_INSERT [dbo].[liquidate] ON 

INSERT [dbo].[liquidate] ([id], [created_at], [updated_at], [done], [time], [user_id]) VALUES (1, CAST(N'2020-12-24 09:46:37.2010000' AS DateTime2), CAST(N'2020-12-24 09:46:37.2010000' AS DateTime2), 0, CAST(N'2020-12-29 14:00:00.0000000' AS DateTime2), 1)
SET IDENTITY_INSERT [dbo].[liquidate] OFF
SET IDENTITY_INSERT [dbo].[liquidate_material] ON 

INSERT [dbo].[liquidate_material] ([id], [created_at], [updated_at], [liquidate_id], [material_id]) VALUES (1, CAST(N'2020-12-25 23:09:47.8650000' AS DateTime2), CAST(N'2020-12-25 23:09:47.8650000' AS DateTime2), 1, 1)
SET IDENTITY_INSERT [dbo].[liquidate_material] OFF
SET IDENTITY_INSERT [dbo].[material] ON 

INSERT [dbo].[material] ([id], [created_at], [updated_at], [credential_code], [is_have_include], [parent_code], [status], [time_start_depreciation], [additional_id], [current_place_id], [product_id], [user_id]) VALUES (1, CAST(N'2020-12-24 09:47:42.6200000' AS DateTime2), CAST(N'2020-12-28 22:37:19.2180000' AS DateTime2), N'Cred-11111', 0, NULL, N'REQUEST_LIQUIDATE', CAST(N'2020-12-12 14:00:00.0000000' AS DateTime2), 1, 1, 1, NULL)
INSERT [dbo].[material] ([id], [created_at], [updated_at], [credential_code], [is_have_include], [parent_code], [status], [time_start_depreciation], [additional_id], [current_place_id], [product_id], [user_id]) VALUES (2, CAST(N'2020-12-24 09:47:42.8350000' AS DateTime2), CAST(N'2020-12-24 09:47:42.8350000' AS DateTime2), N'Cred-22222', 0, NULL, N'IN_USED', CAST(N'2020-12-13 14:00:00.0000000' AS DateTime2), 1, 1, 1, NULL)
INSERT [dbo].[material] ([id], [created_at], [updated_at], [credential_code], [is_have_include], [parent_code], [status], [time_start_depreciation], [additional_id], [current_place_id], [product_id], [user_id]) VALUES (3, CAST(N'2020-12-24 09:47:42.8820000' AS DateTime2), CAST(N'2020-12-24 09:47:42.8820000' AS DateTime2), N'Cred-44444', 0, NULL, N'IN_USED', CAST(N'2020-12-13 14:00:00.0000000' AS DateTime2), 1, 2, 2, NULL)
INSERT [dbo].[material] ([id], [created_at], [updated_at], [credential_code], [is_have_include], [parent_code], [status], [time_start_depreciation], [additional_id], [current_place_id], [product_id], [user_id]) VALUES (4, CAST(N'2020-12-24 09:47:42.9040000' AS DateTime2), CAST(N'2020-12-24 09:47:42.9040000' AS DateTime2), N'Cred-33333', 0, NULL, N'IN_USED', CAST(N'2020-12-12 14:00:00.0000000' AS DateTime2), 1, 2, 2, NULL)
INSERT [dbo].[material] ([id], [created_at], [updated_at], [credential_code], [is_have_include], [parent_code], [status], [time_start_depreciation], [additional_id], [current_place_id], [product_id], [user_id]) VALUES (5, CAST(N'2020-12-25 22:50:21.0440000' AS DateTime2), CAST(N'2020-12-25 22:50:21.0440000' AS DateTime2), N'cred-666666', 0, NULL, N'IN_USED', CAST(N'2020-12-25 21:11:38.9630000' AS DateTime2), 1, 2, 3, NULL)
INSERT [dbo].[material] ([id], [created_at], [updated_at], [credential_code], [is_have_include], [parent_code], [status], [time_start_depreciation], [additional_id], [current_place_id], [product_id], [user_id]) VALUES (6, CAST(N'2020-12-25 22:50:21.2390000' AS DateTime2), CAST(N'2020-12-25 22:50:21.2390000' AS DateTime2), N'cread-77777', 0, NULL, N'IN_USED', CAST(N'2019-01-25 21:11:38.9630000' AS DateTime2), 1, 2, 3, NULL)
INSERT [dbo].[material] ([id], [created_at], [updated_at], [credential_code], [is_have_include], [parent_code], [status], [time_start_depreciation], [additional_id], [current_place_id], [product_id], [user_id]) VALUES (7, CAST(N'2020-12-25 22:50:21.2650000' AS DateTime2), CAST(N'2020-12-25 22:50:21.2650000' AS DateTime2), N'Cred-55555', 0, NULL, N'IN_USED', CAST(N'2020-12-25 21:11:38.9630000' AS DateTime2), 1, 2, 3, NULL)
SET IDENTITY_INSERT [dbo].[material] OFF
SET IDENTITY_INSERT [dbo].[organization] ON 

INSERT [dbo].[organization] ([id], [created_at], [updated_at], [contact], [name]) VALUES (1, CAST(N'2020-12-24 09:40:19.4030000' AS DateTime2), CAST(N'2020-12-24 09:40:19.4030000' AS DateTime2), N'Giám đốc điều hành : 0931061891', N'CT-TGDD')
INSERT [dbo].[organization] ([id], [created_at], [updated_at], [contact], [name]) VALUES (2, CAST(N'2020-12-25 22:43:34.9790000' AS DateTime2), CAST(N'2020-12-25 22:43:34.9790000' AS DateTime2), N'Giám đốc 0978888xx', N'AAAA')
SET IDENTITY_INSERT [dbo].[organization] OFF
SET IDENTITY_INSERT [dbo].[place] ON 

INSERT [dbo].[place] ([id], [created_at], [updated_at], [code], [description], [direction], [floor], [name_specification], [campus_id], [department_id], [type_place_id]) VALUES (1, CAST(N'2020-12-24 09:44:02.7310000' AS DateTime2), CAST(N'2020-12-24 09:44:02.7310000' AS DateTime2), N'B14', N'Phòng học với 100 chỗ ngồi', N'Dãy B, lầu 3, gần cuối dãy', 2, N'Phòng Học B14', 1, 1, 1)
INSERT [dbo].[place] ([id], [created_at], [updated_at], [code], [description], [direction], [floor], [name_specification], [campus_id], [department_id], [type_place_id]) VALUES (2, CAST(N'2020-12-24 09:44:31.3860000' AS DateTime2), CAST(N'2020-12-24 09:44:31.3860000' AS DateTime2), N'B16', N'Phòng thực hành 50 chỗ ngồi', N'Dãy B, lầu 3, cuối dãy', 2, N'Phòng Thực Hành B16', 1, 1, 1)
SET IDENTITY_INSERT [dbo].[place] OFF
SET IDENTITY_INSERT [dbo].[product] ON 

INSERT [dbo].[product] ([id], [created_at], [updated_at], [allocation_duration], [depreciation_rate], [description], [name], [origin], [time_allocation_type], [type], [calculation_unit], [category_id]) VALUES (1, CAST(N'2020-12-24 09:45:19.5790000' AS DateTime2), CAST(N'2020-12-24 09:45:19.5790000' AS DateTime2), 5, 20, N'Máy Chiếu Panasonic (Có màn chiếu)', N'Product1', N'Japan', N'YEAR', N'ASSET', 1, 5)
INSERT [dbo].[product] ([id], [created_at], [updated_at], [allocation_duration], [depreciation_rate], [description], [name], [origin], [time_allocation_type], [type], [calculation_unit], [category_id]) VALUES (2, CAST(N'2020-12-24 09:45:41.3150000' AS DateTime2), CAST(N'2020-12-24 09:45:41.3150000' AS DateTime2), 12, 8.3333333333333321, N'Máy Chiếu Samsung (Không màn chiếu)', N'Product2', N'Japan', N'MONTH', N'TOOL', 1, 5)
INSERT [dbo].[product] ([id], [created_at], [updated_at], [allocation_duration], [depreciation_rate], [description], [name], [origin], [time_allocation_type], [type], [calculation_unit], [category_id]) VALUES (3, CAST(N'2020-12-25 22:46:08.8690000' AS DateTime2), CAST(N'2020-12-25 22:46:08.8690000' AS DateTime2), 4, 25, N'sản phẩm3', N'Product3', N'china', N'YEAR', N'ASSET', 1, 11)
SET IDENTITY_INSERT [dbo].[product] OFF
SET IDENTITY_INSERT [dbo].[roles] ON 

INSERT [dbo].[roles] ([id], [role_name]) VALUES (2, N'ROLE_ACCOUNTANT')
INSERT [dbo].[roles] ([id], [role_name]) VALUES (1, N'ROLE_ADMIN')
INSERT [dbo].[roles] ([id], [role_name]) VALUES (3, N'ROLE_EMPLOYEE')
INSERT [dbo].[roles] ([id], [role_name]) VALUES (4, N'ROLE_INSPECTOR')
SET IDENTITY_INSERT [dbo].[roles] OFF
SET IDENTITY_INSERT [dbo].[transfer_material] ON 

INSERT [dbo].[transfer_material] ([id], [created_at], [updated_at], [reason], [time], [material_id], [place_from_id], [place_target_id], [user_id]) VALUES (1, CAST(N'2020-12-24 10:15:40.3510000' AS DateTime2), CAST(N'2020-12-24 10:15:40.3510000' AS DateTime2), N'Thiếu nên bổ sung', CAST(N'2020-12-24 10:15:22.3760000' AS DateTime2), 1, NULL, 2, 1)
INSERT [dbo].[transfer_material] ([id], [created_at], [updated_at], [reason], [time], [material_id], [place_from_id], [place_target_id], [user_id]) VALUES (2, CAST(N'2020-12-28 22:37:19.0950000' AS DateTime2), CAST(N'2020-12-28 22:37:19.0950000' AS DateTime2), N'Bổ sung theo nhu cầu', CAST(N'2020-12-28 22:36:58.1240000' AS DateTime2), 1, 2, 1, 1)
SET IDENTITY_INSERT [dbo].[transfer_material] OFF
SET IDENTITY_INSERT [dbo].[type_place] ON 

INSERT [dbo].[type_place] ([id], [created_at], [updated_at], [description], [name]) VALUES (1, CAST(N'2020-12-24 09:42:59.8380000' AS DateTime2), CAST(N'2020-12-24 09:42:59.8380000' AS DateTime2), N'Phòng học', N'PHONGHOC')
INSERT [dbo].[type_place] ([id], [created_at], [updated_at], [description], [name]) VALUES (2, CAST(N'2020-12-24 09:43:09.5630000' AS DateTime2), CAST(N'2020-12-24 09:43:09.5630000' AS DateTime2), N'Phòng thực hành', N'PHONGTH')
SET IDENTITY_INSERT [dbo].[type_place] OFF
INSERT [dbo].[user_role] ([user_id], [role_id]) VALUES (1, 1)
INSERT [dbo].[user_role] ([user_id], [role_id]) VALUES (1, 2)
INSERT [dbo].[user_role] ([user_id], [role_id]) VALUES (1, 3)
INSERT [dbo].[user_role] ([user_id], [role_id]) VALUES (1, 4)
INSERT [dbo].[user_role] ([user_id], [role_id]) VALUES (3, 3)
INSERT [dbo].[user_role] ([user_id], [role_id]) VALUES (3, 4)
SET IDENTITY_INSERT [dbo].[users] ON 

INSERT [dbo].[users] ([id], [created_at], [updated_at], [active], [email], [full_name], [password], [phone], [username], [department_id]) VALUES (1, CAST(N'2020-12-24 09:39:06.2590000' AS DateTime2), CAST(N'2020-12-28 21:12:38.8350000' AS DateTime2), 1, N'voduykhanhnc@gmail.com', N'Võ Duy Khánh', N'$2a$10$PBNJrL85cyYEmz31BofF0.H68oExMBhmumwGuX9dWna8fVRXqqGay', N'0931061891', N'duykhanh', 1)
INSERT [dbo].[users] ([id], [created_at], [updated_at], [active], [email], [full_name], [password], [phone], [username], [department_id]) VALUES (3, CAST(N'2020-12-24 09:42:43.7420000' AS DateTime2), CAST(N'2020-12-28 00:33:01.9300000' AS DateTime2), 1, N'thanhphong@gmail.com', N'Nguyễn Thanh Phong', N'$2a$10$vdJmdChOc.fle3aQ2n1NyegCQ3I0p8d52se0XB17oxOuhgI2m40yq', N'01236141101', N'thanhphong', 1)
SET IDENTITY_INSERT [dbo].[users] OFF
/****** Object:  Index [UK_7vgpb8innntdwagneb75lujnh]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[additional] ADD  CONSTRAINT [UK_7vgpb8innntdwagneb75lujnh] UNIQUE NONCLUSTERED 
(
	[time] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [UK7tv0fqkh2nmtdnes1p3o1j4f9]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[additional_product] ADD  CONSTRAINT [UK7tv0fqkh2nmtdnes1p3o1j4f9] UNIQUE NONCLUSTERED 
(
	[additional_id] ASC,
	[product_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_tii3g7wc6gwdxyoqg9iixky7u]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[calculation_unit] ADD  CONSTRAINT [UK_tii3g7wc6gwdxyoqg9iixky7u] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_81rhu9trr93hw9082igcywgl8]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[campus] ADD  CONSTRAINT [UK_81rhu9trr93hw9082igcywgl8] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_gnw9rha5q0xuasc6u00s7r5jg]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[campus] ADD  CONSTRAINT [UK_gnw9rha5q0xuasc6u00s7r5jg] UNIQUE NONCLUSTERED 
(
	[contact_email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_r5arl3wxy9dkgpwwlcgfwhv4r]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[campus] ADD  CONSTRAINT [UK_r5arl3wxy9dkgpwwlcgfwhv4r] UNIQUE NONCLUSTERED 
(
	[contact_phone] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_46ccwnsi9409t36lurvtyljak]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[category] ADD  CONSTRAINT [UK_46ccwnsi9409t36lurvtyljak] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_1t68827l97cwyxo9r1u6t4p7d]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[department] ADD  CONSTRAINT [UK_1t68827l97cwyxo9r1u6t4p7d] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_16fame6je5oyjncqmbl1n5177]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[groups] ADD  CONSTRAINT [UK_16fame6je5oyjncqmbl1n5177] UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [UK_hovujyb0ylc9yfgdvixtvh0w1]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[inventory] ADD  CONSTRAINT [UK_hovujyb0ylc9yfgdvixtvh0w1] UNIQUE NONCLUSTERED 
(
	[time] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [UKkr0vov4p3xvaelxl1sq5kawyr]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[inventory_material] ADD  CONSTRAINT [UKkr0vov4p3xvaelxl1sq5kawyr] UNIQUE NONCLUSTERED 
(
	[inventory_id] ASC,
	[material_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [UKtg4ph0weovkcle71ddb08bbav]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[inventory_material] ADD  CONSTRAINT [UKtg4ph0weovkcle71ddb08bbav] UNIQUE NONCLUSTERED 
(
	[inventory_id] ASC,
	[material_id] ASC,
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [UK_b1altweymi6kymfbhtalunru9]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[liquidate] ADD  CONSTRAINT [UK_b1altweymi6kymfbhtalunru9] UNIQUE NONCLUSTERED 
(
	[time] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [UK_o62onyfih7oakg9iuqf1uur85]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[liquidate_material] ADD  CONSTRAINT [UK_o62onyfih7oakg9iuqf1uur85] UNIQUE NONCLUSTERED 
(
	[material_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
/****** Object:  Index [UKej6nr1ediw6l886bew5h7nago]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[liquidate_material] ADD  CONSTRAINT [UKej6nr1ediw6l886bew5h7nago] UNIQUE NONCLUSTERED 
(
	[liquidate_id] ASC,
	[material_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_2en3vw9sqw80gnwnghthv3cs2]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[material] ADD  CONSTRAINT [UK_2en3vw9sqw80gnwnghthv3cs2] UNIQUE NONCLUSTERED 
(
	[credential_code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_8j5y8ipk73yx2joy9yr653c9t]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[organization] ADD  CONSTRAINT [UK_8j5y8ipk73yx2joy9yr653c9t] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_r186fpmygnarrcd201o1vxe8j]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[place] ADD  CONSTRAINT [UK_r186fpmygnarrcd201o1vxe8j] UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_jmivyxk9rmgysrmsqw15lqr5b]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[product] ADD  CONSTRAINT [UK_jmivyxk9rmgysrmsqw15lqr5b] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_716hgxp60ym1lifrdgp67xt5k]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[roles] ADD  CONSTRAINT [UK_716hgxp60ym1lifrdgp67xt5k] UNIQUE NONCLUSTERED 
(
	[role_name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_banqeasch6xvtvbih2ean0naj]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[type_place] ADD  CONSTRAINT [UK_banqeasch6xvtvbih2ean0naj] UNIQUE NONCLUSTERED 
(
	[name] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_6dotkott2kjsp8vw4d0m25fb7]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [UK_6dotkott2kjsp8vw4d0m25fb7] UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_du5v5sr43g5bfnji4vb8hg5s3]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [UK_du5v5sr43g5bfnji4vb8hg5s3] UNIQUE NONCLUSTERED 
(
	[phone] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
SET ANSI_PADDING ON

GO
/****** Object:  Index [UK_r43af9ap4edm43mmtq01oddj6]    Script Date: 28-Dec-20 11:20:13 PM ******/
ALTER TABLE [dbo].[users] ADD  CONSTRAINT [UK_r43af9ap4edm43mmtq01oddj6] UNIQUE NONCLUSTERED 
(
	[username] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [dbo].[additional]  WITH CHECK ADD  CONSTRAINT [FKet3kdibyco51douuuferdc9gg] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[additional] CHECK CONSTRAINT [FKet3kdibyco51douuuferdc9gg]
GO
ALTER TABLE [dbo].[additional]  WITH CHECK ADD  CONSTRAINT [FKt1alv9hpj5ani25xa533pfmja] FOREIGN KEY([organization_id])
REFERENCES [dbo].[organization] ([id])
GO
ALTER TABLE [dbo].[additional] CHECK CONSTRAINT [FKt1alv9hpj5ani25xa533pfmja]
GO
ALTER TABLE [dbo].[additional_product]  WITH CHECK ADD  CONSTRAINT [FK4bp7wkl65eg475u7p0ntomr45] FOREIGN KEY([product_id])
REFERENCES [dbo].[product] ([id])
GO
ALTER TABLE [dbo].[additional_product] CHECK CONSTRAINT [FK4bp7wkl65eg475u7p0ntomr45]
GO
ALTER TABLE [dbo].[additional_product]  WITH CHECK ADD  CONSTRAINT [FKnp0b6fmawn2abu44pk4a626qq] FOREIGN KEY([additional_id])
REFERENCES [dbo].[additional] ([id])
GO
ALTER TABLE [dbo].[additional_product] CHECK CONSTRAINT [FKnp0b6fmawn2abu44pk4a626qq]
GO
ALTER TABLE [dbo].[category]  WITH CHECK ADD  CONSTRAINT [FKiieibfvwo09cle4lxta1ltqek] FOREIGN KEY([group_id])
REFERENCES [dbo].[groups] ([id])
GO
ALTER TABLE [dbo].[category] CHECK CONSTRAINT [FKiieibfvwo09cle4lxta1ltqek]
GO
ALTER TABLE [dbo].[inventory_material]  WITH CHECK ADD  CONSTRAINT [FK5qsj30m3psgl1hoalnkcwx82t] FOREIGN KEY([material_id])
REFERENCES [dbo].[material] ([id])
GO
ALTER TABLE [dbo].[inventory_material] CHECK CONSTRAINT [FK5qsj30m3psgl1hoalnkcwx82t]
GO
ALTER TABLE [dbo].[inventory_material]  WITH CHECK ADD  CONSTRAINT [FK9pa2m6hc8dvdyitrksx2is76j] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[inventory_material] CHECK CONSTRAINT [FK9pa2m6hc8dvdyitrksx2is76j]
GO
ALTER TABLE [dbo].[inventory_material]  WITH CHECK ADD  CONSTRAINT [FKmhql0ttsmec7mhdq1t3m62yyw] FOREIGN KEY([inventory_id])
REFERENCES [dbo].[inventory] ([id])
GO
ALTER TABLE [dbo].[inventory_material] CHECK CONSTRAINT [FKmhql0ttsmec7mhdq1t3m62yyw]
GO
ALTER TABLE [dbo].[liquidate]  WITH CHECK ADD  CONSTRAINT [FK4ahjfmci7ip34afirwk8wjnwd] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[liquidate] CHECK CONSTRAINT [FK4ahjfmci7ip34afirwk8wjnwd]
GO
ALTER TABLE [dbo].[liquidate_material]  WITH CHECK ADD  CONSTRAINT [FKmhpyesj1t2d35xmhjc4td2ux2] FOREIGN KEY([liquidate_id])
REFERENCES [dbo].[liquidate] ([id])
GO
ALTER TABLE [dbo].[liquidate_material] CHECK CONSTRAINT [FKmhpyesj1t2d35xmhjc4td2ux2]
GO
ALTER TABLE [dbo].[liquidate_material]  WITH CHECK ADD  CONSTRAINT [FKplvgspnupoetiy5i2a0lyn93q] FOREIGN KEY([material_id])
REFERENCES [dbo].[material] ([id])
GO
ALTER TABLE [dbo].[liquidate_material] CHECK CONSTRAINT [FKplvgspnupoetiy5i2a0lyn93q]
GO
ALTER TABLE [dbo].[material]  WITH CHECK ADD  CONSTRAINT [FK6ps0e9pqttae1ygohma94q2r5] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[material] CHECK CONSTRAINT [FK6ps0e9pqttae1ygohma94q2r5]
GO
ALTER TABLE [dbo].[material]  WITH CHECK ADD  CONSTRAINT [FKausejg8uiebrhdat6ak37jagk] FOREIGN KEY([additional_id])
REFERENCES [dbo].[additional] ([id])
GO
ALTER TABLE [dbo].[material] CHECK CONSTRAINT [FKausejg8uiebrhdat6ak37jagk]
GO
ALTER TABLE [dbo].[material]  WITH CHECK ADD  CONSTRAINT [FKge9hhe8jxcmkynun8igiypa75] FOREIGN KEY([current_place_id])
REFERENCES [dbo].[place] ([id])
GO
ALTER TABLE [dbo].[material] CHECK CONSTRAINT [FKge9hhe8jxcmkynun8igiypa75]
GO
ALTER TABLE [dbo].[material]  WITH CHECK ADD  CONSTRAINT [FKmkxedk79wsqc47bm4hm0c4jfw] FOREIGN KEY([product_id])
REFERENCES [dbo].[product] ([id])
GO
ALTER TABLE [dbo].[material] CHECK CONSTRAINT [FKmkxedk79wsqc47bm4hm0c4jfw]
GO
ALTER TABLE [dbo].[place]  WITH CHECK ADD  CONSTRAINT [FKqyvyl0141jaamwxromhrv9iu0] FOREIGN KEY([department_id])
REFERENCES [dbo].[department] ([id])
GO
ALTER TABLE [dbo].[place] CHECK CONSTRAINT [FKqyvyl0141jaamwxromhrv9iu0]
GO
ALTER TABLE [dbo].[place]  WITH CHECK ADD  CONSTRAINT [FKs9jxnngmo9yrdkepl784c9v30] FOREIGN KEY([type_place_id])
REFERENCES [dbo].[type_place] ([id])
GO
ALTER TABLE [dbo].[place] CHECK CONSTRAINT [FKs9jxnngmo9yrdkepl784c9v30]
GO
ALTER TABLE [dbo].[place]  WITH CHECK ADD  CONSTRAINT [FKtnvala5eibf0cv7fbad0i3a3h] FOREIGN KEY([campus_id])
REFERENCES [dbo].[campus] ([id])
GO
ALTER TABLE [dbo].[place] CHECK CONSTRAINT [FKtnvala5eibf0cv7fbad0i3a3h]
GO
ALTER TABLE [dbo].[product]  WITH CHECK ADD  CONSTRAINT [FK1mtsbur82frn64de7balymq9s] FOREIGN KEY([category_id])
REFERENCES [dbo].[category] ([id])
GO
ALTER TABLE [dbo].[product] CHECK CONSTRAINT [FK1mtsbur82frn64de7balymq9s]
GO
ALTER TABLE [dbo].[product]  WITH CHECK ADD  CONSTRAINT [FKk34cppr38wshuikl72d8mnvqh] FOREIGN KEY([calculation_unit])
REFERENCES [dbo].[calculation_unit] ([id])
GO
ALTER TABLE [dbo].[product] CHECK CONSTRAINT [FKk34cppr38wshuikl72d8mnvqh]
GO
ALTER TABLE [dbo].[transfer_material]  WITH CHECK ADD  CONSTRAINT [FK4tqflfcldfpk2e6ly75loggi7] FOREIGN KEY([place_target_id])
REFERENCES [dbo].[place] ([id])
GO
ALTER TABLE [dbo].[transfer_material] CHECK CONSTRAINT [FK4tqflfcldfpk2e6ly75loggi7]
GO
ALTER TABLE [dbo].[transfer_material]  WITH CHECK ADD  CONSTRAINT [FKh6k82y3uusggkjrdjyooeh5ap] FOREIGN KEY([place_from_id])
REFERENCES [dbo].[place] ([id])
GO
ALTER TABLE [dbo].[transfer_material] CHECK CONSTRAINT [FKh6k82y3uusggkjrdjyooeh5ap]
GO
ALTER TABLE [dbo].[transfer_material]  WITH CHECK ADD  CONSTRAINT [FKr9taxmn4yd35kssgo9h8hjyo6] FOREIGN KEY([material_id])
REFERENCES [dbo].[material] ([id])
GO
ALTER TABLE [dbo].[transfer_material] CHECK CONSTRAINT [FKr9taxmn4yd35kssgo9h8hjyo6]
GO
ALTER TABLE [dbo].[transfer_material]  WITH CHECK ADD  CONSTRAINT [FKs4oor2bkpderr1fnrejryox0w] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[transfer_material] CHECK CONSTRAINT [FKs4oor2bkpderr1fnrejryox0w]
GO
ALTER TABLE [dbo].[user_role]  WITH CHECK ADD  CONSTRAINT [FKj345gk1bovqvfame88rcx7yyx] FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([id])
GO
ALTER TABLE [dbo].[user_role] CHECK CONSTRAINT [FKj345gk1bovqvfame88rcx7yyx]
GO
ALTER TABLE [dbo].[user_role]  WITH CHECK ADD  CONSTRAINT [FKt7e7djp752sqn6w22i6ocqy6q] FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([id])
GO
ALTER TABLE [dbo].[user_role] CHECK CONSTRAINT [FKt7e7djp752sqn6w22i6ocqy6q]
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD  CONSTRAINT [FKfi832e3qv89fq376fuh8920y4] FOREIGN KEY([department_id])
REFERENCES [dbo].[department] ([id])
GO
ALTER TABLE [dbo].[users] CHECK CONSTRAINT [FKfi832e3qv89fq376fuh8920y4]
GO
/****** Object:  StoredProcedure [dbo].[STATISTICAL_INVENTORY]    Script Date: 28-Dec-20 11:20:13 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROC [dbo].[STATISTICAL_INVENTORY](@categoryId BIGINT, @year INT)
AS
BEGIN
	SELECT CAST(E1.name AS VARCHAR) AS name,
		   CAST(E1.credential_code AS VARCHAR) AS code,
		   CASE WHEN P.id IS NOT NULL THEN CAST(P.name_specification AS NVARCHAR) ELSE ('No_Place') END AS place,
		   A_P.price AS price,
		   E1.time_allocation_type AS timeAllocationType,
		   E1.allocation_duration AS allocationDuration,
		   E1.depreciation_rate AS depreciationRate,
		   E1.time_start_depreciation AS timeStartDepreciation,
		   E1.status AS materialStatus,
		   CASE WHEN U.ID_MATERIAL IS NOT NULL THEN ('Checked') ELSE ('Un_Checked') END AS materialCheck
	FROM
	(
		SELECT  dbo.product.id,
				dbo.product.name,
				dbo.material.credential_code,
				dbo.product.time_allocation_type,
				dbo.product.allocation_duration,
				dbo.product.depreciation_rate,
				dbo.material.id AS M_ID,
				dbo.material.time_start_depreciation,
				dbo.material.status,
				dbo.material.current_place_id

				FROM dbo.material
				JOIN dbo.product
				ON product.id = material.product_id
				JOIN dbo.category
				ON category.id = product.category_id
				WHERE category_id = @categoryId
	) AS E1
	LEFT JOIN dbo.additional_product AS A_P
		ON A_P.product_id = E1.id
	LEFT JOIN dbo.place AS P
		ON P.id = E1.current_place_id
	LEFT JOIN 
	(SELECT dbo.inventory_material.material_id AS ID_MATERIAL
		FROM dbo.inventory_material
		JOIN dbo.inventory ON inventory.id = inventory_material.inventory_id
		WHERE YEAR(dbo.inventory.time) = @year
	) AS U
	ON E1.M_ID = U.ID_MATERIAL

END
GO
USE [master]
GO
ALTER DATABASE [springboot_asset_dev] SET  READ_WRITE 
GO
