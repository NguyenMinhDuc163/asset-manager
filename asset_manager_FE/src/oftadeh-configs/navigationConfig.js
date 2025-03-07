const navigationConfig = [
  {
    id: "dd",
    title: "DANH MỤC CHÍNH",
    type: "group",
    children: [
      {
        id: "organization",
        title: "Tổ chức",
        type: "item",
        icon: "apartment",
        url: "/organization",
        exact: true,
      },
      {
        id: "campus",
        title: "Khuôn viên",
        type: "item",
        icon: "where_to_vote",
        url: "/campus",
        exact: true,
      },
     
      {
        id: "department",
        title: "Phòng ban",
        type: "item",
        icon: "location_city",
        url: "/department",
        exact: true,
      },
     
    {
    id: "taisan",
    title: "Tài sản - CCDC",
    type: "group",
    children: [
      {
        id: "12",
        title: "Cơ sở vật chất",
        type: "item",
        icon: "home_work",
        url: "/material",
        exact: true,
      },
      {
        id: "product",
        title: "Sản phẩm",
        type: "item",
        icon: "ballot",
        url: "/product",
        exact: true,
      },
      {
        id: "vitri ",
        title: "Vị trí",
        type: "collapse",
        icon: "source",
        badge: {
          title: "2",
          bg: "#525E8A",
          fg: "#FFFFFF",
        },
        children: [
          {
            id: "9",
            title: "Vị trí",
            type: "item",
            icon: "gps_fixed",
            url: "/place",
            exact: true,
          },
          {
            id: "typeplace",
            title: "Kiểu vị trí",
            type: "item",
            icon: "filter_list",
            url: "/typeplace",
            exact: true,
          },
        ],
      },
      {
        id: "Quanlydot ",
        title: "Quản lý đợt",
        type: "collapse",
        icon: "source",
        badge: {
          title: "3",
          bg: "#525E8A",
          fg: "#FFFFFF",
        },
        children: [
          {
            id: "10",
            title: "Đợt thanh lý",
            type: "item",
            icon: "settings_backup_restore",
            url: "/liquidate",
            exact: true,
          },
          {
            id: "11",
            title: "Đợt kiểm kê",
            type: "item",
            icon: "functions",
            url: "/inventory",
            exact: true,
          },
          {
            id: "AdditionalConfig",
            title: "Đợt bổ sung",
            type: "item",
            icon: "timer",
            url: "/additional",
            exact: true,
          },
        ],
      },        
    ],
  },
      {
        id: "group",
        title: "Nhóm ",
        type: "item",
        icon: "widgets",
        url: "/group",
        exact: true,
      },
      {
        id: "category",
        title: "Danh mục",
        type: "item",
        icon: "category",
        url: "/category",
        exact: true,
      },
      {
        id: "calculationUnit",
        title: "Đơn vị tính",
        type: "item",
        icon: "graphic_eq",
        url: "/calculationUnit",
        exact: true,
      },
    ],
  },
  {
    id: "system",
    title: "KHÁC",
    type: "group",
    children: [
      {
        id: "Hethong",
        title: "Hệ thống",
        type: "collapse",
        icon: "settings",
        badge: {
          title: "2",
          bg: "#525E8A",
          fg: "#FFFFFF",
        },
        children: [
          {
            id: "8",
            title: "User",
            type: "item",
            icon: "people_alt",
            url: "/user",
            exact: true,
          },
          {
            id: "9",
            title: "Backup và Restore",
            type: "item",
            icon: "cached",
            url: "/backup",
            exact: true,
          },
         
        ],
      },
    ],
  },
  {
    id: "divider-1",
    type: "divider",
  },
  {
    id: "about",
    title: "Giới thiệu",
    type: "group",
    children: [
      {
        id: "Team K&P",
        title: "Team K&P",
        type: "link",
        icon: "link",
        url: "https://github.com/PhongMax/asset-management",
        exact: true,
      },
    ],
  },
];

export default navigationConfig;
